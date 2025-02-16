package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.QCombo.combo
import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.player.QPlayer.player
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ComboQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun findByGameDate(memberId: Long, gameDate: LocalDate): ComboResponse? {
        return queryFactory
            .select(QComboResponse(
                combo.id,
                player.id,
                player.name,
                player.playerImage.imageUrl,
                combo.comboStatus
            ))
            .from(combo)
            .leftJoin(player).on(player.id.eq(combo.playerId))
            .where(combo.gameDate.eq(gameDate)
                .and(combo.memberId.eq(memberId)))
            .fetchOne()
    }
}

data class ComboResponse @QueryProjection constructor(
    val comboId: Long,
    val playerId: Long,
    val playerName: String,
    val playerImageUrl: String?,
    val comboStatus: ComboStatus
)
