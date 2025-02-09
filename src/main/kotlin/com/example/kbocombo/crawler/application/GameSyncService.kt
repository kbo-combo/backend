package com.example.kbocombo.crawler.application

import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class GameSyncService(
    private val gameClient: GameClient,
    private val gameRenewService: GameRenewService
) {

    fun syncGame(gameDate: LocalDate, now: LocalDateTime) {
        val games = gameClient.findGames(gameDate)
        gameRenewService.renewGame(games = games, gameDate = gameDate, now = now)
    }
}
