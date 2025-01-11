package com.example.kbocombo.domain.player.infra

import com.example.kbocombo.domain.player.presentation.request.PlayerSomethingRequest
import com.example.kbocombo.player.Player
import com.example.kbocombo.player.QPlayer.player
import com.example.kbocombo.player.vo.PlayerPosition
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class PlayerQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun findAllHitterByParam(request: PlayerSomethingRequest): List<Player> {
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
