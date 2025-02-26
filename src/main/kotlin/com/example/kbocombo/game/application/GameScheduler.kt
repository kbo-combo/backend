package com.example.kbocombo.game.application

import com.example.kbocombo.crawler.game.application.GameClient
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.infra.GameEndEventJobRepository
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class GameScheduler(
    private val gameEndEventJobRepository: GameEndEventJobRepository,
    private val gameEndEventJobService: GameEndEventJobService,
    private val gameClient: GameClient,
    private val publisher: ApplicationEventPublisher
) {

    @Scheduled(fixedDelay = 600000L)
    fun scheduleGameEndEventJob() {
        val now = LocalDateTime.now()
        val today = now.toLocalDate()

        val processableJobIds = gameEndEventJobRepository
            .findAllByProcessedAndGameDate(processed = false, gameDate = today)
            .filter { it.createdDateTime.isBefore(now.minusMinutes(10)) }
            .map { it.id }

        processableJobIds.forEach { gameEndEventJobService.process(it!!) }
    }

    @Scheduled(fixedDelay = 600000L)
    fun scheduleTodayGames() {
        val now = LocalDateTime.now()
        val today = now.toLocalDate()

        val todayGames = gameClient.findGames(today)

        for (todayGame in todayGames) {
            val gameState = todayGame.gameState

            when (gameState) {
                GameState.RUNNING -> publisher.publishEvent(GameRunningEvent(gameId = todayGame.id))
                GameState.COMPLETED -> publisher.publishEvent(GameCompletedEvent(gameId = todayGame.id))
                GameState.CANCEL -> publisher.publishEvent(GameCanceledEvent(gameId = todayGame.id))
                GameState.PENDING -> {}
            }
        }
    }
}

data class GameRunningEvent(
    val gameId: Long
)

data class GameCompletedEvent(
    val gameId: Long
)

data class GameCanceledEvent(
    val gameId: Long
)
