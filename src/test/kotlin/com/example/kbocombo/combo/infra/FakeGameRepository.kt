package com.example.kbocombo.combo.infra

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.mock.infra.BaseFakeRepository

class FakeGameRepository : BaseFakeRepository<Game>(Game::class), GameRepository {

    override fun findById(gameId: Long): Game? {
        return db.find { it.id == gameId }
    }
}
