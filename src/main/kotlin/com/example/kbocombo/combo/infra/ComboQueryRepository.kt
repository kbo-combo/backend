package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.domain.QCombo.combo
import com.example.kbocombo.game.domain.QGame.game
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.domain.QPlayer.player
import com.example.kbocombo.record.domain.HitterGameRecord
import com.example.kbocombo.record.domain.QHitterGameRecord.hitterGameRecord
import com.example.kbocombo.utils.before
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

    fun findOneByParams(memberId: Long, gameDate: LocalDate?, gameId: Long?): ComboDetailQueryDto? {
        return queryFactory
            .select(QComboDetailQueryDto(combo, player, hitterGameRecord))
            .from(combo)
            .leftJoin(combo.game, game).fetchJoin()
            .leftJoin(player).on(player.id.eq(combo.playerId))
            .leftJoin(hitterGameRecord).on(hitterGameRecord.gameId.eq(game.id)
                .and(hitterGameRecord.playerId.eq(player.id)))
            .where(
                BooleanBuilder()
                    .and(combo.memberId.eq(memberId))
                    .and(eq(combo.game.id, gameId))
                    .and(eq(combo.gameDate, gameDate))
            )
            .fetchOne()
    }


    fun findAllComboByParams(memberId: Long, beforeGameDate: LocalDate?, gameType: GameType?, pageSize: Long): List<ComboListQueryDto> {
        return queryFactory
            .select(QComboListQueryDto(combo, player, hitterGameRecord))
            .from(combo)
            .leftJoin(combo.game, game).fetchJoin()
            .leftJoin(player).on(player.id.eq(combo.playerId))
            .leftJoin(hitterGameRecord).on(hitterGameRecord.gameId.eq(game.id)
                .and(hitterGameRecord.playerId.eq(player.id)))
            .where(
                BooleanBuilder()
                    .and(combo.memberId.eq(memberId))
                    .and(eq(combo.game.gameType, gameType))
                    .and(before(combo.gameDate, beforeGameDate))
            )
            .limit(pageSize)
            .orderBy(combo.gameDate.desc())
            .fetch()
    }
}

data class ComboDetailQueryDto @QueryProjection constructor(
    val combo: Combo,
    val player: Player,
    val hitterGameRecord: HitterGameRecord?
)

data class ComboListQueryDto @QueryProjection constructor(
    val combo: Combo,
    val player: Player,
    val hitterGameRecord: HitterGameRecord?
)
