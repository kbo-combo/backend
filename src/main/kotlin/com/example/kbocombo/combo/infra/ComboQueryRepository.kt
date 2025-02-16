package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.domain.QCombo.combo
import com.example.kbocombo.player.Player
import com.example.kbocombo.player.QPlayer.player
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import org.springframework.stereotype.Repository

@Repository
class ComboQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun findByComboParams(
        memberId: Long,
        gameId: Long?,
        gameDate: LocalDate?
    ): ComboQueryDto? {
        return queryFactory
            .select(
                QComboQueryDto(
                    combo, player
                )
            )
            .from(combo)
            .join(player).on(combo.playerId.eq(player.id))
            .where(
                combo.memberId.eq(memberId),
                eqGameId(gameId),
                eqGameDate(gameDate)
            )
            .fetchOne()
    }

    private fun eqGameId(gameId: Long?): BooleanExpression? {
        if (gameId == null) {
            return null
        }
        return combo.game.id.eq(gameId)
    }

    private fun eqGameDate(gameDate: LocalDate?): BooleanExpression? {
        if (gameDate == null) {
            return null
        }
        return combo.game.startDate.eq(gameDate)
    }
}

data class ComboQueryDto @QueryProjection constructor(
    val combo: Combo,
    val player: Player
)

