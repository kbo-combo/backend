package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.combo.infra.ComboDetailQueryDto
import com.example.kbocombo.combo.infra.ComboListQueryDto
import com.example.kbocombo.combo.infra.ComboQueryRepository
import com.example.kbocombo.common.dto.SliceResponse
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.player.vo.Team
import com.example.kbocombo.record.domain.HitterGameRecord
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime

@Service
class ComboQueryService(
    private val comboQueryRepository: ComboQueryRepository
) {

    @Transactional(readOnly = true)
    fun findOneByParams(memberId: Long, gameDate: LocalDate?, gameId: Long?): ComboDetailResponse? {
        val dto = comboQueryRepository.findOneByParams(memberId = memberId, gameDate = gameDate, gameId = gameId)
        return dto?.let {  ComboDetailResponse.from(dto)}
    }

    @Transactional(readOnly = true)
    fun findAllComboByParams(
        memberId: Long,
        beforeGameDate: LocalDate?,
        gameType: GameType?,
        pageSize: Long
    ): SliceResponse<ComboListResponse> {
        val dtos = comboQueryRepository.findAllComboByParams(
            memberId = memberId,
            beforeGameDate = beforeGameDate,
            gameType = gameType,
            pageSize = pageSize + 1
        )
        return SliceResponse.of(ComboListResponse.toList(dtos), pageSize)
    }
}

data class ComboDetailResponse(
    val comboId: Long,
    val playerId: Long,
    val playerName: String,
    val playerImageUrl: String?,
    val comboStatus: ComboStatus,
    val gameStartDate: LocalDate,
    val gameStartTime: LocalTime,
    val hitterGameRecord: HitterGameRecordDto?
) {
    companion object {

        fun from(queryDto: ComboDetailQueryDto): ComboDetailResponse {
            val combo = queryDto.combo
            val game = combo.game
            val player = queryDto.player
            return ComboDetailResponse(
                comboId = combo.id,
                playerId = player.id,
                playerName = player.name,
                playerImageUrl = player.playerImage.imageUrl,
                comboStatus = combo.comboStatus,
                gameStartDate = game.startDate,
                gameStartTime = game.startTime,
                hitterGameRecord = HitterGameRecordDto.from(queryDto.hitterGameRecord)
            )
        }
    }
}

data class ComboListResponse(
    val comboId: Long,
    val playerId: Long,
    val playerName: String,
    val playerImageUrl: String?,
    val comboStatus: ComboStatus,
    val gameStartDate: LocalDate,
    val gameStartTime: LocalTime,
    val gameType: GameType,
    val homeTeam: Team,
    val awayTeam: Team,
    val hitterGameRecord: HitterGameRecordDto?
) {
    companion object {

        fun toList(dtos: List<ComboListQueryDto>): List<ComboListResponse> {
            return dtos.map {
                val combo = it.combo
                val player = it.player
                val game = combo.game
                ComboListResponse(
                    comboId = combo.id,
                    playerId = player.id,
                    playerName = player.name,
                    playerImageUrl = player.playerImage.imageUrl,
                    comboStatus = combo.comboStatus,
                    gameStartDate = game.startDate,
                    gameStartTime = game.startTime,
                    homeTeam = game.homeTeam,
                    awayTeam = game.awayTeam,
                    gameType = game.gameType,
                    hitterGameRecord = HitterGameRecordDto.from(it.hitterGameRecord)
                )
            }
        }
    }
}

data class HitterGameRecordDto(
    val hits: Int,
    val atBats: Int,
) {
    companion object {

        fun from(hitterGameRecord: HitterGameRecord?): HitterGameRecordDto? {
            return hitterGameRecord?.let { return  HitterGameRecordDto(atBats = it.atBats, hits = it.hits)}
        }
    }
}
