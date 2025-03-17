package com.example.kbocombo.game.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.common.logWarn
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameScore
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameService(
    private val gameRepository: GameRepository,
) {
    @Transactional
    fun run(gameId: Long) {
        val game = gameRepository.getById(gameId)

        if (game.isRunning().not()) {
            game.start()
            gameRepository.save(game)
        }
    }

    @Transactional
    fun complete(gameId: Long): Game {
        val game = gameRepository.getById(gameId)
        if (game.isCompleted()) {
            logInfo("Game is already completed, gameId: $gameId")
            return game
        }
        game.complete()
        return gameRepository.save(game)
    }

    @Transactional
    fun cancel(gameId: Long): Game {
        val game = gameRepository.getById(gameId)
        if (game.isCancelled()) {
            logInfo("Game is already canceled, gameId: $gameId")
            return game
        }

        game.cancel()
        return gameRepository.save(game)
    }

    @Transactional
    fun updateGameScore(gameCode: String, gameScore: GameScore) {
        val game = gameRepository.findByGameCode(gameCode)
            ?: run {
                logWarn("Game does not exist in DB, gameCode: $gameCode")
                return
            }
        game.updateGameScore(gameScore)
    }
}
