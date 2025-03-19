package com.example.kbocombo.mock.infra

import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.vo.WebId

class FakePlayerRepository : BaseFakeRepository<Player>(Player::class), PlayerRepository {

    override fun findAllByIsRetiredFalse(): List<Player> {
        return db.filterNot { it.isRetired }
    }

     override fun findById(playerId: Long): Player? {
         return db.find { it.id == playerId }
     }

    override fun findByWebId(webId: WebId): Player? {
        return db.find { it.webId == webId }
    }

    override fun findAllByWebIdIn(webIds: List<Long>): List<Player> {
        return db.filter { webIds.contains(it.webId.value)}
    }

    override fun findAllByIdIn(ids: List<Long>): List<Player> {
        return db.filter { ids.contains(it.id) }
    }
}
