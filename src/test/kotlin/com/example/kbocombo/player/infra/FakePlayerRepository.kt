package com.example.kbocombo.player.infra

import com.example.kbocombo.mock.infra.BaseFakeRepository
import com.example.kbocombo.player.Player

class FakePlayerRepository : BaseFakeRepository<Player>(Player::class), PlayerRepository {

    override fun findAllByIsRetiredFalse(): List<Player> {
        return db.filterNot { it.isRetired }
    }
}
