package com.example.kbocombo.crawler.service

import com.example.kbocombo.domain.player.Player

interface PlayerClient {

    fun findAllNewPlayers(savedPlayers: List<Player>) : List<Player>
}
