package com.example.kbocombo.combo.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.record.domain.HitterHitRecordedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ComboHandler(
    private val comboService: ComboService
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW)
    fun handleHitterHitRecordedEvent(hitterHitRecordedEvent: HitterHitRecordedEvent) {
        val gameId = hitterHitRecordedEvent.gameId
        val playerId = hitterHitRecordedEvent.playerId
        logInfo("Player Id: $playerId hits in game: $gameId ")

        comboService.updateComboToSuccess(gameId = gameId, playerId = playerId)
    }
}
