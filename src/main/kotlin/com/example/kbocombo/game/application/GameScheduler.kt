package com.example.kbocombo.game.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.crawler.game.application.GameClient
import com.example.kbocombo.crawler.game.application.GameSyncService
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.infra.GameEndEventJobRepository
import com.example.kbocombo.game.infra.GameRepository
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
    private val gameRepository: GameRepository,
    private val gameService: GameService,
    private val gameHandler: GameHandler
) {

    @Scheduled(cron = "0 0/10 * * * ?")
    fun scheduleGameEndEventJob() {
        val now = LocalDateTime.now()

        val processableJobIds = gameEndEventJobRepository
            .findAllByProcessed(processed = false)
            .filter { it.createdDateTime.isBefore(now.minusMinutes(10)) }
            .map { it.id }

        processableJobIds.forEach { gameEndEventJobService.process(it!!, now) }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
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
                GameState.RUNNING -> gameHandler.runGame(gameId = savedTodayGame.id)
                GameState.COMPLETED -> gameHandler.completeGame(gameId = savedTodayGame.id, gameScore = todayGameDto.gameScore)
                GameState.CANCEL -> gameHandler.cancelGame(gameId = savedTodayGame.id)
                GameState.PENDING -> {}
            }
        }
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    fun schedulePostDateGame() {
        val today = LocalDate.now()
        val now = LocalDateTime.now()
        for (i in 0..6) {
            val targetDate = today.plusDays(i.toLong())
            gameSyncService.syncGame(targetDate, now)
            logInfo("renew post date game, game date is $targetDate")
        }
    }

    @Scheduled(cron = "0 0/1 13-23 * * ?")
    fun scheduleGameScore() {
        val today = LocalDate.now()
        val gameDtos = gameClient.findGames(today)
        for (gameDto in gameDtos) {
            gameService.updateGameScore(gameCode = gameDto.gameCode, gameScore = gameDto.gameScore)
        }
    }
}
