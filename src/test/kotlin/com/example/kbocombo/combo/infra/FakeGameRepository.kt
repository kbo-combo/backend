package com.example.kbocombo.combo.infra

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.mock.infra.BaseFakeRepository
import java.time.LocalDate

class FakeGameRepository : BaseFakeRepository<Game>(Game::class), GameRepository {

    override fun findById(gameId: Long): Game? {
        return db.find { it.id == gameId }
    }

    override fun findAllByStartDate(startDate: LocalDate): List<Game> {
        return db.filter { it.startDate == startDate }
    }
}
