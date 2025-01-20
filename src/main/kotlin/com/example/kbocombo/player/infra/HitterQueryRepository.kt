package com.example.kbocombo.player.infra

import com.example.kbocombo.player.Player
import com.example.kbocombo.player.QPlayer.player
import com.example.kbocombo.player.presentation.request.HitterComboQueryRequest
import com.example.kbocombo.player.vo.PlayerPosition
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class HitterQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun findAllHitterByTeam(request: HitterComboQueryRequest): List<Player> {
        return queryFactory
            .selectFrom(player)
            .where(
                player.team.`in`(request.homeTeam, request.awayTeam)
                    .and(player.position.eq(PlayerPosition.HITTER))
                    .and(player.isRetired.isFalse)
            )
            .fetch()
    }

}
