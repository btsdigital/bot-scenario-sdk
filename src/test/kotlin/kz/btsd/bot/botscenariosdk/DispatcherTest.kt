package kz.btsd.bot.botscenariosdk

import kz.btsd.bot.botscenariosdk.utils.messengerId
import kz.btsd.messenger.bot.api.model.peer.PeerUser
import kz.btsd.messenger.bot.api.model.update.Message
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZonedDateTime

class DispatcherTest {
    @Test
    fun messengerIdLockTest(){
       val update1 = Message("updateId", "messageId", ZonedDateTime.now(), PeerUser("userId"),
               PeerUser("userId"), "content")
        val update2 = Message("updateId", "messageId", ZonedDateTime.now(), PeerUser("userId"),
                PeerUser("userId"), "content")
        assertTrue(update1.messengerId===update2.messengerId)
    }

}
