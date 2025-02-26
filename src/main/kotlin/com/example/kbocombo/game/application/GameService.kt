package com.example.kbocombo.game.application

import com.example.kbocombo.common.logInfo
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
    fun complete(gameId: Long) {
        val game = gameRepository.getById(gameId)
        if (game.isCompleted()) {
            throw IllegalStateException("Game is already completed, gameId: $gameId")
        }
        game.complete()
        gameRepository.save(game)
    }

    @Transactional
    fun cancel(gameId: Long) {
        val game = gameRepository.getById(gameId)
        if (game.isCancelled()) {
            throw IllegalStateException("Game is canceled, gameId: $gameId")
        }

        game.cancel()
        gameRepository.save(game)
    }
}
