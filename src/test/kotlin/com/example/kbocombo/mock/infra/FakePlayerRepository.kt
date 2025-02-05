package com.example.kbocombo.mock.infra

import com.example.kbocombo.player.Player
import com.example.kbocombo.player.infra.PlayerRepository

class FakePlayerRepository : BaseFakeRepository<Player>(Player::class), PlayerRepository {

    override fun findAllByIsRetiredFalse(): List<Player> {
        return db.filterNot { it.isRetired }
    }

     override fun findById(playerId: Long): Player? {
         return db.find { it.id == playerId }
     }
 }
