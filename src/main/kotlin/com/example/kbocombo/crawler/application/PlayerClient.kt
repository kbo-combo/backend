package com.example.kbocombo.crawler.application

import com.example.kbocombo.player.Player

interface PlayerClient {

    fun getRecentPlayers() : List<Player>
}
