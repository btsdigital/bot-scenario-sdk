package kz.btsd.bot.botscenariosdk.session

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@ConditionalOnProperty(prefix = "session.service", name = ["url"])
class SessionService(restTemplateBuilder: RestTemplateBuilder,
                     @Value("\${session.service.url}") private val baseUrl: String
) {
    private val sessionTemplate: RestTemplate = restTemplateBuilder.build()


    fun getSession(botId: String, messengerId: String): SessionStored? {
        val session = this.sessionTemplate.getForObject("$baseUrl/$botId/$messengerId", SessionStored::class.java)
        session?.sessionService = this
        return session
    }

    fun postSession(botId: String, messengerId: String, session: SessionStored): SessionStored? {
        return this.sessionTemplate.postForObject(
                "$baseUrl/$botId/$messengerId", ObjectMapper().writeValueAsString(session), SessionStored::class.java
        )
    }

    fun deleteSession(botId: String, messengerId: String) {
        return this.sessionTemplate.delete("$baseUrl/$botId/$messengerId")
    }
}
