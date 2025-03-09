package com.example.kbocombo.record.application

import com.example.kbocombo.record.infra.HitterGameRecordRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class HitterGameRecordService(
    private val hitterGameRecordRepository: HitterGameRecordRepository
) {

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun deleteAllHitterRecordByGame(event: GameDomainCanceledEvent) {
        hitterGameRecordRepository.deleteByGameId(event.gameId)
    }
}



data class GameDomainCanceledEvent(val gameId: Long)
