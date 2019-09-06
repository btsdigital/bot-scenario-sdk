package kz.btsd.bot.botscenariosdk.session

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

import java.io.IOException

class SessionSerializer @JvmOverloads constructor(t: Class<SessionStored>? = null) : StdSerializer<SessionStored>(t) {

    @Throws(IOException::class)
    override fun serialize(value: SessionStored, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("messengerId", value.messengerId)
        gen.writeStringField("botId", value.botId)
        for(key: String in value.keys) {
            gen.writeObjectField(key, value[key])
        }
        gen.writeEndObject()
    }
}
