package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.dto.NewPlayerData

interface PlayerCrawler {

    fun getPlayers(): List<NewPlayerData>
}
