package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.application.request.ComboCreateRequest
import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.combo.infra.ComboRepository
import com.example.kbocombo.combo.infra.getById
import com.example.kbocombo.common.logInfo
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.infra.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ComboService(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val comboRepository: ComboRepository,
) {

    @Transactional
    fun createCombo(request: ComboCreateRequest, memberId: Long, now: LocalDateTime) {
        val game = gameRepository.getById(request.gameId)
        val player = playerRepository.getById(request.playerId)
        val sameDateCombo = findSameDateCombo(memberId, game)
        val combo = sameDateCombo?.apply {
            update(game = game, playerId = player.id, now = now)
        } ?: Combo(
            game = game,
            memberId = memberId,
            playerId = player.id,
            now = now
        )
        comboRepository.save(combo)
    }

    @Transactional
    fun deleteCombo(comboId: Long, now: LocalDateTime) {
        val combo = comboRepository.getById(comboId)
        combo.checkDelete(now)
        comboRepository.delete(combo)
    }

    @Transactional
    fun updateComboToSuccess(gameId: Long, playerId: Long) {
        val game = gameRepository.getById(gameId = gameId)
        if (game.isRunning().not()) {
            logInfo("진행 중인 게임이 아닌 경우, 콤보 성공 처리를 할 수 없습니다.")
            return
        }
        val player = playerRepository.getById(playerId = playerId)
        val combos = comboRepository.findAllByGameAndPlayerIdAndComboStatus(
            game = game,
            playerId = player.id,
            comboStatus = ComboStatus.PENDING
        )
        combos.forEach {
            it.success()
            comboRepository.save(it)
        }
    }

    @Transactional
    fun updateComboToFail(gameId: Long) {
        val game = gameRepository.getById(gameId)
        if (game.isCompleted().not()) {
            throw IllegalArgumentException("게임이 종료되지 않은 경우, 콤보 실패 처리를 할 수 없습니다.")
        }

        val combos = comboRepository.findAllByGameAndComboStatus(
            game = game,
            comboStatus = ComboStatus.PENDING
        )

        combos.forEach {
            it.fail()
            comboRepository.save(it)
        }
    }

    @Transactional
    fun updateComboToPass(gameId: Long, now: LocalDateTime) {
        val game = gameRepository.getById(gameId)
        if (game.isCancelled().not()) {
            throw IllegalArgumentException("게임이 취소되지 않은 경우, 콤보 패스 처리를 할 수 없습니다.")
        }

        val combos = comboRepository.findAllByGame(game = game)
            .filter { it.isPassed().not()  }

        combos.forEach {
            it.pass()
            comboRepository.save(it)
        }
    }

    private fun findSameDateCombo(memberId: Long, game: Game): Combo? {
        return comboRepository.findByMemberIdAndGameDate(memberId, game.startDate)
    }
}
