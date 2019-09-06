package kz.btsd.bot.botscenariosdk.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@ConfigurationProperties(prefix = "sdk.scenario")
class ScenarioSdkConfig {

    var defaultErrorMessage :String = "incorrect input, try again"
    var defaultRequestEnumErrorMessage: String = "incorrect input, select option"

}
