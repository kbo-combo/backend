package com.example.kbocombo.crawler.infra

import com.example.kbocombo.crawler.application.PlayerClient
import com.example.kbocombo.player.Player
import org.springframework.stereotype.Component

@Component
class KboPlayerCrawler(
    private val kboPlayerDetailPageParser: KboPlayerDetailPageParser,
    private val kboPlayerListPageCrawler: KboPlayerListPageCrawler
) : PlayerClient {

    override fun getRecentPlayers(): List<Player> {
        val players = kboPlayerListPageCrawler.getPlayers()
        return players.mapNotNull(kboPlayerDetailPageParser::getPlayerProfile)
    }
}