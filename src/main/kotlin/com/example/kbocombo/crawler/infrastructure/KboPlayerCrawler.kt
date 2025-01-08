package com.example.kbocombo.crawler.infrastructure

import com.example.kbocombo.crawler.service.PlayerClient
import com.example.kbocombo.domain.player.Player
import org.springframework.stereotype.Component

@Component
class KboPlayerCrawler(
    private val kboPlayerDetailPageParser: KboPlayerDetailPageParser,
    private val playerCrawler: PlayerCrawler
) : PlayerClient {

    override fun findAllPlayers(): List<Player> {
        val players = playerCrawler.getPlayers()
        return kboPlayerDetailPageParser.getPlayerProfile(players)
    }
}
