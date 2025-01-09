package com.example.kbocombo.player.fake

import com.example.kbocombo.player.Player
import java.util.concurrent.atomic.AtomicLong

class FakePlayerRepository : PlayerRepository {

    val db = mutableListOf<Player>()
    private val idGenerator = AtomicLong(1)

    override fun save(player: Player): Player {
        val newPlayer = if (player.id == 0L) {
            val newId = idGenerator.getAndIncrement()
            setId(player, newId)
        } else {
            player
        }

        db.add(newPlayer)
        return newPlayer
    }

    override fun findAllByRetiredNot(): List<Player> {
        return db.filterNot { it.isRetired }
    }

    private fun setId(player: Player, newId: Long): Player {
        val idField = Player::class.java.getDeclaredField("id") // 필드 가져오기
        idField.isAccessible = true
        idField.set(player, newId)
        return player
    }
}
