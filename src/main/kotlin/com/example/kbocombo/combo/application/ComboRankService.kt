package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.ComboRank
import com.example.kbocombo.combo.infra.ComboRankRepository
import com.example.kbocombo.member.infra.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ComboRankService(
    private val memberRepository: MemberRepository,
    private val comboRankRepository: ComboRankRepository
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
