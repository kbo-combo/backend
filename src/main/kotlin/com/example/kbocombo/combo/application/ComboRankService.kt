package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.domain.ComboRank
import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.combo.infra.ComboRankQueryRepository
import com.example.kbocombo.combo.infra.ComboRankRepository
import com.example.kbocombo.combo.infra.ComboRepository
import com.example.kbocombo.combo.infra.TopRankQueryDto
import com.example.kbocombo.common.logInfo
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.member.infra.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ComboRankService(
    private val memberRepository: MemberRepository,
    private val comboRepository: ComboRepository,
    private val comboRankRepository: ComboRankRepository,
    private val comboRankQueryRepository: ComboRankQueryRepository
) {

    @Transactional
    fun process(game: Game) {
        if (game.isRunning()) {
            logInfo("아직 게임이 완료되지 않았습니다. gameId: ${game.id}")
            return
        }

        val combos = comboRepository.findAllByGame(game)
        combos.forEach { recordToRank(it) }
    }

    private fun recordToRank(combo: Combo) {
        val comboRank = findComboRankOrSave(memberId = combo.memberId, year = combo.gameDate.year)

        when (combo.comboStatus) {
            ComboStatus.SUCCESS -> comboRank.recordComboSuccess(gameDate = combo.gameDate)
            ComboStatus.FAIL -> comboRank.recordComboFail()
            ComboStatus.PASS -> comboRank.recordComboPass()
            ComboStatus.PENDING -> {}
        }
        comboRankRepository.save(comboRank)
    }

    private fun findComboRankOrSave(memberId: Long, year: Int): ComboRank {
        return comboRankRepository.findByMemberIdAndYear(memberId = memberId, year = year)
            ?: comboRankRepository.save(
                ComboRank.init(
                    memberId = memberId, years = year
                )
            )
    }

    @Transactional
    fun create(memberId: Long) {
        val member = memberRepository.findById(memberId)

        val comboRank = ComboRank.init(
            memberId = member.id,
            years = LocalDate.now().year
        )
        comboRankRepository.save(comboRank)
    }

    @Transactional(readOnly = true)
    fun getMemberComboRank(memberId: Long): MemberComboRankResponse {
        val member = memberRepository.findById(memberId)

        val comboRank = comboRankRepository.findByMemberId(memberId = member.id)

        return MemberComboRankResponse.from(comboRank)
    }

    @Transactional(readOnly = true)
    fun getComboRankStatistic(year: Int, count: Int): ComboRankStatisticResponse {
        val topRanksDto = comboRankQueryRepository.findTopRanks(year = year, limit = count.toLong())

        var previousScore: Int? = null
        var currentRank = 0
        val topRanks = topRanksDto.mapIndexed { index, dto ->
            // 이전 점수와 다르면 현재 인덱스 + 1이 순위가 됩니다.
            if (previousScore == null || dto.currentRecord != previousScore) {
                currentRank = index + 1
            }
            previousScore = dto.currentRecord
            TopRankResponse.of(dto, currentRank)
        }
        return ComboRankStatisticResponse(topRanks = topRanks)
    }
}

data class MemberComboRankResponse(
    val id: Long,
    val year: Int,
    val memberId: Long,
    val currentRecord: Int,
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
                successCount = comboRank.successCount,
                failCount = comboRank.failCount,
                passCount = comboRank.passCount,
                totalCount = comboRank.totalCount,
                firstSuccessDate = comboRank.firstSuccessDate,
                lastSuccessDate = comboRank.lastSuccessDate
            )
        }
    }
}

data class TopRankResponse(
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
        fun of(topRankQueryDto: TopRankQueryDto, rank: Int): TopRankResponse {
            return TopRankResponse(
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

data class ComboRankStatisticResponse(
    val topRanks: List<TopRankResponse>
)
