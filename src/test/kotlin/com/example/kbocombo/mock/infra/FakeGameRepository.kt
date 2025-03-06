package com.example.kbocombo.mock.infra

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.infra.GameRepository
import java.time.LocalDate

class FakeGameRepository : BaseFakeRepository<Game>(Game::class), GameRepository {

    override fun findById(gameId: Long): Game? {
        return db.find { it.id == gameId }
    }

    override fun findAllByStartDate(startDate: LocalDate): List<Game> {
        return db.filter { it.startDate == startDate }
    }

    override fun findByGameCode(gameCode: String): Game? {
        return db.find { it.gameCode == gameCode }
    }
}
