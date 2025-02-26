package com.example.kbocombo.crawler.game.application

import com.example.kbocombo.crawler.game.infra.GameDto
import com.example.kbocombo.crawler.game.infra.toEntity
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
    fun renewGame(gameDtos: List<GameDto>, gameDate: LocalDate, now: LocalDateTime) {
        val savedGameByCode = gameRepository.findAllByStartDate(gameDate)
            .associateBy { it.gameCode }
        for (gameDto in gameDtos) {
            val savedGame = savedGameByCode[gameDto.gameCode]
            if (savedGame == null) {
                gameRepository.save(gameDto.toEntity())
                continue
            }

            if (savedGame.isAfterGameStart(now)){
                continue
            }

            updateStartingPitcher(
                savedGame = savedGame,
                homeStartingPitcherId = gameDto.homeStartingPitcherId,
                awayStartingPitcherId = gameDto.awayStartingPitcherId
            )
        }
    }

    private fun updateStartingPitcher(savedGame: Game, homeStartingPitcherId: Long?, awayStartingPitcherId: Long?) {
        savedGame.updateStartingPitcher(
            homeStartingPitcherId = homeStartingPitcherId,
            awayStartingPitcherId = awayStartingPitcherId,
        )
    }
}
