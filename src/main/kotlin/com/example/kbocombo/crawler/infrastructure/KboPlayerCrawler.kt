package com.example.kbocombo.crawler.infrastructure

import com.example.kbocombo.crawler.dto.PlayerResponse
import com.example.kbocombo.crawler.service.PlayerClient
import org.springframework.stereotype.Component

@Component
class KboPlayerCrawler(
    private val kboPlayerDetailPageParser: KboPlayerDetailPageParser,
    private val playerCrawler: PlayerCrawler
) : PlayerClient {

    override fun findAllPlayers(): List<PlayerResponse> {
        val players = playerCrawler.getPlayers()
        return kboPlayerDetailPageParser.getPlayerProfile(players)
    }
}
