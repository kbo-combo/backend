package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.domain.QCombo.combo
import com.example.kbocombo.game.domain.QGame.game
import com.example.kbocombo.player.Player
import com.example.kbocombo.player.QPlayer.player
import com.example.kbocombo.utils.eq
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ComboQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun findComboByParams(memberId: Long, gameDate: LocalDate?, gameId: Long?): ComboQueryDto? {
        return queryFactory
            .select(QComboQueryDto(combo, player))
            .from(combo)
            .leftJoin(combo.game, game).fetchJoin()
            .leftJoin(player).on(player.id.eq(combo.playerId))
            .where(
                BooleanBuilder()
                    .and(combo.memberId.eq(memberId))
                    .and(eq(combo.game.id, gameId))
                    .and(eq(combo.gameDate, gameDate))
            )
            .fetchOne()
    }
}

data class ComboQueryDto @QueryProjection constructor(
    val combo: Combo,
    val player: Player
)
