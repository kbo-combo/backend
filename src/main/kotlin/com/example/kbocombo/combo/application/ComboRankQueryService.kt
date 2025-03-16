package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.ComboRank
import com.example.kbocombo.combo.infra.ComboRankQueryRepository
import com.example.kbocombo.combo.infra.ComboRankRepository
import com.example.kbocombo.combo.infra.TopRankQueryDto
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.member.infra.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ComboRankQueryService(
    private val memberRepository: MemberRepository,
    private val comboRankRepository: ComboRankRepository,
    private val comboRankQueryRepository: ComboRankQueryRepository
) {

    @Transactional(readOnly = true)
    fun getMemberComboRank(memberId: Long): List<MemberComboRankByYearResponse> {
        val member = memberRepository.findById(memberId)

        val comboRanks = comboRankRepository.findAllByMemberId(memberId = member.id)

        return MemberComboRankByYearResponse.from(comboRanks)
    }

    @Transactional(readOnly = true)
    fun getComboRankStatistic(year: Int, count: Int, gameType: GameType): TopComboRankResponse {
        val topRanksDto = comboRankQueryRepository.findTopRanks(
            year = year,
            limit = count.toLong(),
            gameType = gameType
        )

        val topRankResponses = calculateRankForDtos(topRanksDto)
        return TopComboRankResponse(gameType = gameType.name, comboRankResponse = topRankResponses)
    }

    private fun calculateRankForDtos(dtos: List<TopRankQueryDto>): List<ComboRankResponse> {
        var previousScore: Int? = null
        var currentRank = 0
        return dtos.mapIndexed { index, dto ->
            if (previousScore == null || dto.currentRecord != previousScore) {
                currentRank = index + 1
            }
            previousScore = dto.currentRecord
            ComboRankResponse.of(dto, currentRank)
        }
    }
}

data class MemberComboRankByYearResponse(
    val year: Int,
    val comboRanks: MemberComboRankByGameType
) {
    companion object {
        fun from(comboRanks: List<ComboRank>): List<MemberComboRankByYearResponse> {
            return comboRanks.groupBy { it.years }
                .map { (year, listForYear) ->
                    MemberComboRankByYearResponse(
                        year = year,
                        comboRanks = MemberComboRankByGameType.from(listForYear)
                    )
                }
                .sortedByDescending { it.year }
        }
    }
}

data class MemberComboRankByGameType(
    val preSeason: MemberComboRankResponse?,
    val regularSeason: MemberComboRankResponse?,
    val postSeason: MemberComboRankResponse?
) {
    companion object {
        fun from(comboRanks: List<ComboRank>): MemberComboRankByGameType {
            val map = comboRanks.associateBy { it.gameType }
            return MemberComboRankByGameType(
                preSeason = map[GameType.PRE_SEASON]?.let { MemberComboRankResponse.from(it) },
                regularSeason = map[GameType.REGULAR_SEASON]?.let { MemberComboRankResponse.from(it) },
                postSeason = map[GameType.POST_SEASON]?.let { MemberComboRankResponse.from(it) },
            )
        }
    }
}

data class MemberComboRankResponse(
    val id: Long,
    val year: Int,
    val memberId: Long,
    val gameType: GameType,
    val currentRecord: Int,
    val maxRecord: Int,
    val successCount: Int,
    val failCount: Int,
    val passCount: Int,
    val totalCount: Int,
    val firstSuccessDate: LocalDate?,
    val lastSuccessDate: LocalDate?
) {
    companion object {
        fun from(comboRank: ComboRank): MemberComboRankResponse {
            return MemberComboRankResponse(
                id = comboRank.id,
                year = comboRank.years,
                memberId = comboRank.memberId,
                currentRecord = comboRank.currentRecord,
                maxRecord = comboRank.maxRecord,
                successCount = comboRank.successCount,
                failCount = comboRank.failCount,
                passCount = comboRank.passCount,
                totalCount = comboRank.totalCount,
                firstSuccessDate = comboRank.firstSuccessDate,
                lastSuccessDate = comboRank.lastSuccessDate,
                gameType = comboRank.gameType
            )
        }
    }
}

data class TopComboRankResponse(
    val gameType: String,
    val comboRankResponse: List<ComboRankResponse>
)

data class ComboRankResponse(
    val rank: Int,
    val id: Long,
    val year: Int,
    val memberId: Long,
    val nickname: String,
    val currentRecord: Int,
    val successCount: Int,
    val failCount: Int,
    val passCount: Int,
    val totalCount: Int,
    val firstSuccessDate: LocalDate?,
    val lastSuccessDate: LocalDate?
) {
    companion object {
        fun of(topRankQueryDto: TopRankQueryDto, rank: Int): ComboRankResponse {
            return ComboRankResponse(
                rank = rank,
                id = topRankQueryDto.id,
                year = topRankQueryDto.year,
                memberId = topRankQueryDto.memberId,
                nickname = topRankQueryDto.nickname,
                currentRecord = topRankQueryDto.currentRecord,
                successCount = topRankQueryDto.successCount,
                failCount = topRankQueryDto.failCount,
                passCount = topRankQueryDto.passCount,
                totalCount = topRankQueryDto.totalCount,
                firstSuccessDate = topRankQueryDto.firstSuccessDate,
                lastSuccessDate = topRankQueryDto.lastSuccessDate
            )
        }
    }
}
