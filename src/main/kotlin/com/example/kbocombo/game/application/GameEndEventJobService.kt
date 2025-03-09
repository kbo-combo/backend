package com.example.kbocombo.game.application

import com.example.kbocombo.combo.application.ComboRankService
import com.example.kbocombo.combo.application.ComboService
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.infra.GameEndEventJobRepository
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameEndEventJobService(
    private val gameRepository: GameRepository,
    private val gameEndEventJobRepository: GameEndEventJobRepository,
    private val comboService: ComboService,
    private val comboRankService: ComboRankService
) {
    
    @Transactional
    fun process(gameEndEventJobId: Long) {
        val gameEndEventJob = gameEndEventJobRepository.findById(gameEndEventJobId)
        val game = gameRepository.getById(gameEndEventJob.gameId)

        when (game.gameState) {
            GameState.COMPLETED -> comboService.updateComboToFail(gameId = game.id)
            GameState.CANCEL -> comboService.updateComboToPass(gameId = game.id)
            else -> { }
        }
        comboRankService.process(game = game)

        gameEndEventJob.finish()
        gameEndEventJobRepository.save(gameEndEventJob)
    }
}
