package kz.btsd.bot.botscenariosdk.session

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import mu.KotlinLogging

@JsonSerialize(using = SessionSerializer::class)
open class SessionStored(
        messengerId: String,
        val botId: String,
        var sessionService: SessionService?
) : Session(messengerId) {

    private val log = KotlinLogging.logger {}

    override fun put(key: String, value: Any): Any? {
        val temp = super.put(key, value)
        sessionService?.postSession(botId, messengerId, this)
        log.debug { "Put session for botId=$botId messengerId=$messengerId. key=$key, value=$value;" }
        return temp
    }

    override fun putAll(from: Map<out String, Any>) {
        super.putAll(from)
        sessionService?.postSession(botId, messengerId, this)
        log.debug { "Put All session for botId=$botId messengerId=$messengerId. ${from.map { "key=${it.key}, " +
                "value=${it.value};" }}" }
    }

    override fun putIfAbsent(key: String, value: Any): Any? {
        val temp = super.putIfAbsent(key, value)
        sessionService?.postSession(botId, messengerId, this)
        log.debug { "Put if absent session for botId=$botId messengerId=$messengerId. key=$key, value=$value;" }
        return temp
    }

    override fun remove(key: String): Any? {
        val temp = super.remove(key)
        sessionService?.deleteSession(botId, messengerId)
        log.debug { "Remove session for botId=$botId messengerId=$messengerId. key=$key" }
        return temp
    }

}
