package com.example.kbocombo.combo.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.crawler.game.infra.HitterHitRecordedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ComboHandler(
    private val comboService: ComboService
) {

    @Async
    @EventListener
    fun handleHitterHitRecordedEvent(hitterHitRecordedEvent: HitterHitRecordedEvent) {
        val gameId = hitterHitRecordedEvent.gameId
        val playerCode = hitterHitRecordedEvent.playerCode
        logInfo("Player code: $playerCode hits in game: $gameId ")

        comboService.updateComboToSuccess(gameId = gameId, playerWebId = playerCode.toLong())
    }
}
