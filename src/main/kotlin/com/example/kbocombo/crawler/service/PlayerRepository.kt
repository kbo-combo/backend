package com.example.kbocombo.crawler.service

import com.example.kbocombo.domain.player.Player
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface PlayerRepository : Repository<Player, Long> {

    fun findAll(): List<Player>

    fun save(player: Player): Player

    @Query("update Player p set p.imageUrl = :imageUrl where p.id = :playerId")
    fun updateImage(player: Player, imageUrl: String?)
}
