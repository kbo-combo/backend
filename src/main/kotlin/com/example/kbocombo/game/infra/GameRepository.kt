package com.example.kbocombo.game.infra

import com.example.kbocombo.game.domain.Game
import org.springframework.data.repository.Repository
import java.time.LocalDate

fun GameRepository.getById(gameId: Long): Game =
    findById(gameId) ?: error("존재하지 않는 게임입니다.")

interface GameRepository : Repository<Game, Long> {

    fun findById(gameId: Long): Game?

    fun findAllByStartDate(startDate: LocalDate) : List<Game>

    fun save(game: Game): Game

    fun findByGameCode(gameCode: String): Game?
}
