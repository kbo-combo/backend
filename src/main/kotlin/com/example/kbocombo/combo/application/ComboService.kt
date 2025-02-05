package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.application.request.ComboCreateRequest
import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.infra.ComboRepository
import com.example.kbocombo.combo.infra.getById
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

    private fun findSameDateCombo(memberId: Long, game: Game): Combo? {
        return comboRepository.findByMemberIdAndGameDate(memberId, game.startDateTime.toLocalDate())
    }
}
