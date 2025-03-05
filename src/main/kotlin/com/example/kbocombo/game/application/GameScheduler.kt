package com.example.kbocombo.game.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.crawler.game.application.GameClient
import com.example.kbocombo.crawler.game.application.GameSyncService
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.infra.GameEndEventJobRepository
import com.example.kbocombo.game.infra.GameRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class GameScheduler(
    private val gameEndEventJobRepository: GameEndEventJobRepository,
    private val gameEndEventJobService: GameEndEventJobService,
    private val gameClient: GameClient,
    private val gameSyncService: GameSyncService,
    private val publisher: ApplicationEventPublisher,
    private val gameRepository: GameRepository
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

        val todayGameDtos = gameClient.findGames(today)
        for (todayGameDto in todayGameDtos) {
            val savedTodayGame = gameRepository.findByGameCode(todayGameDto.gameCode)
            if (savedTodayGame == null) {
                logInfo("In today game schedule, ${todayGameDto.gameCode} is unknown")
                continue
            }

            val currentGameState = todayGameDto.gameState ?: savedTodayGame.gameState
            when (currentGameState) {
                GameState.RUNNING -> publisher.publishEvent(GameRunningEvent(gameId = savedTodayGame.id))
                GameState.COMPLETED -> publisher.publishEvent(GameCompletedEvent(gameId = savedTodayGame.id, gameDate = savedTodayGame.startDate))
                GameState.CANCEL -> publisher.publishEvent(GameCancelledEvent(gameId = savedTodayGame.id, gameDate = savedTodayGame.startDate))
                GameState.PENDING -> {}
            }
        }
    }

    @Scheduled(fixedDelay = 1000L * 60L * 60L)
    fun schedulePostDateGame() {
        val today = LocalDate.now()
        val now = LocalDateTime.now()
        for (i in 0..6) {
            val targetDate = today.plusDays(i.toLong())
            gameSyncService.syncGame(targetDate, now)
            logInfo("renew post date game, game date is $targetDate")
        }
    }
}

data class GameRunningEvent(
    val gameId: Long
)

data class GameCompletedEvent(
    val gameId: Long,
    val gameDate: LocalDate
)

data class GameCancelledEvent(
    val gameId: Long,
    val gameDate: LocalDate
)
