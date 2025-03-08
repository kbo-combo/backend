package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.QComboRank.comboRank
import com.example.kbocombo.member.domain.QMember.member
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ComboRankQueryRepository(
    private val queryFactory: JPAQueryFactory
) {
    fun findTopRanks(limit: Long): List<TopRankQueryDto> {
        return queryFactory
            .select(
                QTopRankQueryDto(
                    comboRank.id,
                    comboRank.memberId,
                    member.nickname,
                    comboRank.currentRecord,
                    comboRank.successCount,
                    comboRank.failCount,
                    comboRank.passCount,
                    comboRank.totalCount,
                    comboRank.firstSuccessDate,
                    comboRank.lastSuccessDate
                )
            )
            .from(comboRank)
            .join(member).on(comboRank.memberId.eq(member.id))
            .orderBy(comboRank.currentRecord.desc())
            .limit(limit)
            .fetch()
    }

}

data class TopRankQueryDto @QueryProjection constructor(
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
)
