package kz.btsd.bot.botscenariosdk.scenario

import kz.btsd.bot.botscenariosdk.i18n.TranslationService
import kz.btsd.bot.botscenariosdk.session.Session
import kz.btsd.bot.botscenariosdk.utils.BeanClassRegistry
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

private val annotationType = Scenario::class.java

@Component
class ScenarioFactory(
    private val context: ApplicationContext,
    private val translationService: TranslationService?,
    scenarioClassRegistry: ScenarioClassRegistry
) {

    private val scenariosByKeywordByBotClass: Map<Class<*>, Map<String, Class<ScenarioEntryPoint>>> =
        scenarioClassRegistry
            .beanClasses
            .asSequence()
            .map { ScenarioDefinition(it.getAnnotation(annotationType), it) }
            .groupBy { it.botClass }
            .mapValues { (_, botScenarios) ->
                botScenarios
                    .map { getScenarioKeywords(it) to it.type }
                    .flatMap { (keys, type) -> keys.map { it to type } }
                    .toMap()
            }

    fun forBot(botClass: Class<*>) = SingleBotScenarioFactory(botClass, this)

    fun isScenarioKeyword(botClass: Class<*>, keyword: String): Boolean =
        scenariosByKeywordByBotClass[botClass]?.containsKey(keyword)
            ?: false

    fun createScenario(botClass: Class<*>, keyword: String, session: Session): ScenarioEntryPoint {
        val scenarioType = getScenarioType(botClass, keyword)
        return context.getBean(scenarioType).also {
            it.init(session)
        }
    }

    private fun getScenarioType(botClass: Class<*>, keyword: String) =
        scenariosByKeywordByBotClass[botClass]?.get(keyword)
            ?: throw IllegalArgumentException("Scenario not found for botClass=${botClass.name} and keyword=$keyword")

    private fun getScenarioKeywords(definition: ScenarioDefinition): Set<String> {
        return if (translationService == null) {
            setOf(definition.command, definition.keyword)
        } else {
            val translatedKeyWords = definition.keyword
                .takeUnless { it.isBlank() }
                ?.let { translationService.allTranslations(definition.keyword).toSet() }
                ?: emptySet()
            translatedKeyWords + definition.command
        }
    }
}

class SingleBotScenarioFactory(private val botClass: Class<*>, private val factory: ScenarioFactory) {
    fun isScenarioKeyword(keyword: String) = factory.isScenarioKeyword(botClass, keyword)
    fun createScenario(keyword: String, session: Session) = factory.createScenario(botClass, keyword, session)
}

@Component
class ScenarioClassRegistry : BeanClassRegistry<ScenarioEntryPoint>(ScenarioEntryPoint::class)

private class ScenarioDefinition(annotation: Scenario, val type: Class<ScenarioEntryPoint>) {
    val botClass = annotation.botClass.java
    val command = annotation.command
    val keyword = annotation.keyword
}
