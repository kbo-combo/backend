package com.example.kbocombo.crawler.game.application

import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class GameSyncService(
    private val gameClient: GameClient,
    private val gameRenewService: GameRenewService
) {

    fun syncGame(gameDate: LocalDate, now: LocalDateTime) {
        val gameDtos = gameClient.findGames(gameDate = gameDate)
        gameRenewService.renewGame(gameDtos = gameDtos, gameDate = gameDate, now = now)
    }
}
