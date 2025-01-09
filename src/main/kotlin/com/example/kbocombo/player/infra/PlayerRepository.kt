package com.example.kbocombo.player.infra

import com.example.kbocombo.player.Player
import org.springframework.data.repository.Repository

interface PlayerRepository : Repository<Player, Long> {

    fun save(player: Player) : Player

    fun findAllByIsRetiredFalse() : List<Player>
}
