package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.infra.ComboRepository
import com.example.kbocombo.combo.infra.getById
import com.example.kbocombo.combo.ui.request.ComboCreateRequest
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.infra.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.LocalTime

@Service
@Transactional
class ComboService(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val comboRepository: ComboRepository,
) {

    fun createCombo(request: ComboCreateRequest, memberId: Long, now: LocalDateTime) {
        val game = gameRepository.getById(request.gameId)
        val player = playerRepository.getById(request.playerId)
        val existingCombo = findExistingCombo(memberId, game.startDateTime)
        val combo = existingCombo?.apply {
            update(game = game, playerId = player.id, now = now)
        } ?: Combo(
            game = game,
            memberId = memberId,
            playerId = player.id,
            now = now
        )
        comboRepository.save(combo)
    }

    fun deleteCombo(comboId: Long, now: LocalDateTime) {
        val combo = comboRepository.getById(comboId)
        combo.checkDelete(now)
        comboRepository.delete(combo)
    }

    private fun findExistingCombo(memberId: Long, gameStartDateTime: LocalDateTime): Combo? {
        val startOfDay = gameStartDateTime.toLocalDate().atStartOfDay()
        val endOfDay = gameStartDateTime.toLocalDate().atTime(LocalTime.MAX)
        return comboRepository.findByMemberIdAndGameStartDateTimeBetween(memberId, startOfDay, endOfDay)
    }
}
