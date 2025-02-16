package com.example.kbocombo.game.infra

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.QGame.game
import com.example.kbocombo.player.Player
import com.example.kbocombo.player.QPlayer.player
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class GameQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun findAllGameByDate(date: LocalDate): List<Game> {
        return queryFactory
            .selectFrom(game)
            .where(game.startDate.eq(date))
            .fetch()
    }

    fun findAllGameByBetweenDate(start: LocalDate, end: LocalDate): List<LocalDate> {
        return queryFactory
            .select(game.startDate)
            .from(game)
            .where(game.startDate.between(start, end))
            .orderBy(game.startDate.asc())
            .fetch()
    }

    fun findAllPlayerIdIn(ids: List<Long>): List<Player> {
        return queryFactory
            .selectFrom(player)
            .where(player.id.`in`(ids))
            .fetch()
    }
}
