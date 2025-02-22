package com.example.kbocombo.crawler.game.application

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.infra.GameRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class GameRenewService(
    private val gameRepository: GameRepository
) {

    @Transactional
    fun renewGame(games: List<Game>, gameDate: LocalDate, now: LocalDateTime) {
        val savedGameByCode = gameRepository.findAllByStartDate(gameDate)
            .associateBy { it.gameCode }
        for (game in games) {
            val savedGame = savedGameByCode[game.gameCode]
            if (savedGame == null) {
                gameRepository.save(game)
                continue
            }

            if (savedGame.isAfterGameStart(now)){
                continue
            }

            updateStartingPitcher(
                savedGame = savedGame,
                homeStartingPitcherId = game.homeStartingPitcherId,
                awayStartingPitcherId = game.awayStartingPitcherId)
        }
    }

    private fun updateStartingPitcher(savedGame: Game, homeStartingPitcherId: Long?, awayStartingPitcherId: Long?) {
        savedGame.updateStartingPitcher(
            homeStartingPitcherId = homeStartingPitcherId,
            awayStartingPitcherId = awayStartingPitcherId,
        )
    }
}
