package com.example.kbocombo.crawler.infrastructure

import com.example.kbocombo.crawler.dto.NewPlayerData

interface PlayerCrawler {

    fun getPlayers(): List<NewPlayerData>
}
