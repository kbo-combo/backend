package com.example.kbocombo.crawler.application

import com.example.kbocombo.domain.player.Player

interface PlayerClient {

    fun getRecentPlayers() : List<Player>
}
