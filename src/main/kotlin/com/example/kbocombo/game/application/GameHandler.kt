package com.example.kbocombo.game.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.game.domain.GameEndEventJob
import com.example.kbocombo.game.infra.GameEndEventJobRepository
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class GameHandler(
    private val gameRepository: GameRepository,
    private val gameEndEventJobRepository: GameEndEventJobRepository
) {

    @Async
    @EventListener
    fun handleGameEndedEvent(gameEndedEvent: GameEndedEvent) {
        val gameId = gameEndedEvent.gameId
        logInfo("Game ended: $gameId ")

        val game = gameRepository.getById(gameId)
        val gameEndEventJob = GameEndEventJob(
            gameId = gameId,
            gameDate = game.startDate,
        )

        gameEndEventJobRepository.save(gameEndEventJob)
    }
}


