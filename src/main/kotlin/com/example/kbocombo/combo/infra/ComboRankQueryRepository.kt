package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.QComboRank.comboRank
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.member.domain.QMember.member
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ComboRankQueryRepository(
    private val queryFactory: JPAQueryFactory
) {
    fun findTopRanks(
        year: Int,
        limit: Long,
        gameType: GameType,
        comboRankSearchType: ComboRankSearchType
    ): List<TopRankQueryDto> {
        val orderSpecifier = when (comboRankSearchType) {
            ComboRankSearchType.CURRENT_RECORD -> comboRank.currentRecord.desc()
            ComboRankSearchType.MAX_RECORD -> comboRank.maxRecord.desc()
        }

        return queryFactory
            .select(
                QTopRankQueryDto(
                    comboRank.id,
                    comboRank.years,
                    comboRank.memberId,
                    comboRank.gameType,
                    member.nickname,
                    comboRank.currentRecord,
                    comboRank.maxRecord,
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
            .orderBy(orderSpecifier)
            .limit(limit)
            .where(
                comboRank.years.eq(year),
                comboRank.gameType.eq(gameType)
            )
            .fetch()
    }

}

data class TopRankQueryDto @QueryProjection constructor(
    val id: Long,
    val year: Int,
    val memberId: Long,
    val gameType: GameType,
    val nickname: String,
    val currentRecord: Int,
    val maxRecord: Int,
    val successCount: Int,
    val failCount: Int,
    val passCount: Int,
    val totalCount: Int,
    val firstSuccessDate: LocalDate?,
    val lastSuccessDate: LocalDate?
)


enum class ComboRankSearchType {
    CURRENT_RECORD,
    MAX_RECORD
}
