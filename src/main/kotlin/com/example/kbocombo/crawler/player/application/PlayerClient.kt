package com.example.kbocombo.crawler.player.application

import com.example.kbocombo.player.domain.Player

interface PlayerClient {

    fun getRecentPlayers(): List<Player>
}
