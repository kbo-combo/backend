package com.example.kbocombo.player.infra

import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.domain.QPlayer.player
import com.example.kbocombo.player.presentation.request.HitterTeamRequest
import com.example.kbocombo.player.vo.PlayerPosition
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class HitterQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun findAllHitterByTeam(request: HitterTeamRequest): List<Player> {
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
