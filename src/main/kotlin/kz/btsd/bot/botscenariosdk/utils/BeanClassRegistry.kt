package kz.btsd.bot.botscenariosdk.utils

import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import kotlin.reflect.KClass

abstract class BeanClassRegistry<T : Any>(private val clazz: KClass<T>) : BeanFactoryPostProcessor {

    private lateinit var beanClassesInner: List<Class<T>>
    val beanClasses get() = beanClassesInner

    @Suppress("UNCHECKED_CAST")
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val scenarioBeanNames = beanFactory.getBeanNamesForType(clazz.java)
        beanClassesInner = scenarioBeanNames.map {
            val definition = beanFactory.getBeanDefinition(it)
            Class.forName(definition.beanClassName) as Class<T>
        }
    }
}
