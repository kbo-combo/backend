package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.combo.infra.ComboQueryDto
import com.example.kbocombo.combo.infra.ComboQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime

@Service
class ComboQueryService(
    private val comboQueryRepository: ComboQueryRepository
) {

    @Transactional(readOnly = true)
    fun findByGameDate(memberId: Long, gameDate: LocalDate?, gameId: Long?): ComboResponse? {
        val dto = comboQueryRepository.findComboByParams(memberId = memberId, gameDate = gameDate, gameId = gameId)
        return dto?.let {  ComboResponse.of(dto)}
    }
}

data class ComboResponse(
    val comboId: Long,
    val playerId: Long,
    val playerName: String,
    val playerImageUrl: String?,
    val comboStatus: ComboStatus,
    val gameStartDate: LocalDate,
    val gameStartTime: LocalTime,
) {
    companion object {

        fun of(queryDto: ComboQueryDto): ComboResponse {
            val combo = queryDto.combo
            val game = combo.game
            val player = queryDto.player
            return ComboResponse(
                comboId = combo.id,
                playerId = player.id,
                playerName = player.name,
                playerImageUrl = player.playerImage.imageUrl,
                comboStatus = combo.comboStatus,
                gameStartDate = game.startDate,
                gameStartTime = game.startTime,
            )
        }
    }
}
