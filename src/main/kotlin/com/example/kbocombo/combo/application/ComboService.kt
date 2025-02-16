package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.application.request.ComboCreateRequest
import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.infra.ComboQueryDto
import com.example.kbocombo.combo.infra.ComboQueryRepository
import com.example.kbocombo.combo.infra.ComboRepository
import com.example.kbocombo.combo.infra.getById
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.infra.getById
import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ComboService(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val comboRepository: ComboRepository,
    private val comboQueryRepository: ComboQueryRepository,
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

    fun findCombo(
        member: Member,
        gameId: Long?,
        gameDate: LocalDate?
    ): ComboResponse? {
        val comboQueryDto = comboQueryRepository.findByComboParams(
            memberId = member.id,
            gameId = gameId,
            gameDate = gameDate
        )

        return comboQueryDto?.let { ComboResponse.of(it) }
    }

    private fun findSameDateCombo(memberId: Long, game: Game): Combo? {
        return comboRepository.findByMemberIdAndGameDate(memberId, game.startDate)
    }
}

data class ComboResponse(
    val memberId: Long,
    val comboStatus: String,
    val gameId: Long,
    val gameDate: LocalDate,
    val playerId: Long,
    val playerName: String,
    val playerImageUrl: String?,
    val currentComboStack: Int
) {
    companion object {
        fun of(comboQueryDto: ComboQueryDto): ComboResponse {
            val combo = comboQueryDto.combo
            val player = comboQueryDto.player

            return ComboResponse(
                memberId = combo.memberId,
                comboStatus = combo.comboStatus.name,
                gameId = combo.game.id,
                gameDate = combo.gameDate,
                playerId = combo.playerId,
                playerName = player.name,
                playerImageUrl = player.playerImage.imageUrl,
                currentComboStack = 0
            )
        }
    }
}
