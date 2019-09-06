package kz.btsd.bot.botscenariosdk.scenario

import kz.btsd.bot.botscenariosdk.session.Session
import kz.btsd.messenger.bot.api.model.update.Update

interface ScenarioEntryPoint {
    fun init(session: Session)
    suspend fun start(update: Update)
}
