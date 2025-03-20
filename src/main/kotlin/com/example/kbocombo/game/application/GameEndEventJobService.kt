package com.example.kbocombo.game.application

import com.example.kbocombo.combo.application.ComboRankService
import com.example.kbocombo.combo.application.ComboService
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.infra.GameEndEventJobRepository
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import com.example.kbocombo.record.application.HitterGameRecordService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class GameEndEventJobService(
    private val gameRepository: GameRepository,
    private val gameEndEventJobRepository: GameEndEventJobRepository,
    private val hitterGameRecordService: HitterGameRecordService,
    private val comboService: ComboService,
    private val comboRankService: ComboRankService
) {

    @Transactional
    fun process(gameEndEventJobId: Long, now: LocalDateTime) {
        val gameEndEventJob = gameEndEventJobRepository.findById(gameEndEventJobId)
        val game = gameRepository.getById(gameEndEventJob.gameId)

        if (game.isAfterGameStart(now).not()) {
            return
        }

        when (game.gameState) {
            GameState.COMPLETED -> comboService.updateComboToFail(gameId = game.id)
            GameState.CANCEL -> comboService.updateComboToPass(gameId = game.id, LocalDateTime.now())
            else -> {}
        }
        comboRankService.process(game = game)

        gameEndEventJob.finish()
        gameEndEventJobRepository.save(gameEndEventJob)
        hitterGameRecordService.deleteAllHitterRecordIfGameCanceled(game)
    }
}
