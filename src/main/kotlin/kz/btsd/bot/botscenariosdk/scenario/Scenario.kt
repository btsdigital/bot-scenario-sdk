package kz.btsd.bot.botscenariosdk.scenario

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

@Target(CLASS)
@Component
@Scope("prototype")
annotation class Scenario(val botClass: KClass<*>, val command: String, val keyword: String = "")
