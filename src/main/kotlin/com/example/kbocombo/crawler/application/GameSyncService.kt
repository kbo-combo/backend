package com.example.kbocombo.crawler.application

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.player.vo.Team
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class GameSyncService(
    private val gameRepository: GameRepository
) {

    @Transactional
    fun renewGame(gameRequests: List<GameRequest>, gameDate: LocalDate, now: LocalDateTime) {
        val gamesByCode = gameRepository.findAllByStartDate(gameDate)
            .associateBy { it.gameCode }
        for (gameRequest in gameRequests) {
            val savedGame = gamesByCode[gameRequest.gameCode]
            if (savedGame == null) {
                gameRepository.save(gameRequest.toGame())
                continue
            }

            if (savedGame.isAfterGameStart(now)){
                continue
            }

            savedGame.updatePitcher(
                homeStartingPitcherId = gameRequest.homeStartingPitcherId,
                awayStartingPitcherId = gameRequest.awayStartingPitcherId,
            )
        }
    }
}


data class GameRequest(
    val gameCode: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeStartingPitcherId: Long? = null,
    val awayStartingPitcherId: Long? = null,
    val startDate: LocalDate,
    val startTime: LocalTime,
    val gameType: GameType,
    val gameState: GameState,
) {
    fun toGame() : Game {
        return Game(
            gameCode = gameCode,
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            homeStartingPitcherId = homeStartingPitcherId,
            awayStartingPitcherId = awayStartingPitcherId,
            startDate = startDate,
            startTime = startTime,
            gameType = gameType,
            gameState = gameState
        )
    }
}
