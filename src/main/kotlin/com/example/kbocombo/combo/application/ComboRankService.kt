package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.ComboRank
import com.example.kbocombo.combo.infra.ComboRankQueryRepository
import com.example.kbocombo.combo.infra.ComboRankRepository
import com.example.kbocombo.combo.infra.TopRankQueryDto
import com.example.kbocombo.member.infra.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ComboRankService(
    private val memberRepository: MemberRepository,
    private val comboRankRepository: ComboRankRepository,
    private val comboRankQueryRepository: ComboRankQueryRepository
) {

    @Transactional
    fun recordSuccess(memberId: Long) {
        val member = memberRepository.findById(memberId)

        val comboRank = comboRankRepository.findByMemberId(memberId = member.id)
        comboRank.recordComboSuccess()
        comboRankRepository.save(comboRank)
    }

    @Transactional
    fun recordFail(memberId: Long) {
        val member = memberRepository.findById(memberId)

        val comboRank = comboRankRepository.findByMemberId(memberId = member.id)
        comboRank.recordComboFail()
        comboRankRepository.save(comboRank)
    }

    @Transactional
    fun recordPass(memberId: Long) {
        val member = memberRepository.findById(memberId)

        val comboRank = comboRankRepository.findByMemberId(memberId = member.id)
        comboRank.recordComboPass()
        comboRankRepository.save(comboRank)
    }

    @Transactional
    fun create(memberId: Long) {
        val member = memberRepository.findById(memberId)

        val comboRank = ComboRank.init(
            memberId = member.id
        )
        comboRankRepository.save(comboRank)
    }

    fun getMemberComboRank(memberId: Long): MemberComboRankResponse {
        val member = memberRepository.findById(memberId)

        val comboRank = comboRankRepository.findByMemberId(memberId = member.id)

        return MemberComboRankResponse.from(comboRank)
    }

    fun getComboRankStatistic(count: Int): ComboRankStatisticResponse {
        val topRanksDto = comboRankQueryRepository.findTopRanks(count.toLong())

        var previousScore: Int? = null
        var currentRank = 0
        val topRanks = topRanksDto.mapIndexed { index, dto ->
            // 이전 점수와 다르면 현재 인덱스 + 1이 순위가 됩니다.
            if (previousScore == null || dto.currentRecord != previousScore) {
                currentRank = index + 1
            }
            previousScore = dto.currentRecord
            TopRankResponse.from(dto, currentRank)
        }
        return ComboRankStatisticResponse(topRanks = topRanks)
    }
}

data class MemberComboRankResponse(
    val id: Long,
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
        fun from(topRankQueryDto: TopRankQueryDto, rank: Int): TopRankResponse {
            return TopRankResponse(
                rank = rank,
                id = topRankQueryDto.id,
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
