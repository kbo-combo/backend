package com.example.kbocombo.game.infra

import com.example.kbocombo.game.domain.Game
import org.springframework.data.repository.Repository

fun GameRepository.getById(gameId: Long): Game =
    findByGameId(gameId) ?: error("존재하지 않는 게임입니다.")

interface GameRepository : Repository<Game, Long> {

    fun findByGameId(gameId: Long): Game?
}
