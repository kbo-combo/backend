package com.example.kbocombo.crawler.infrastructure

import com.example.kbocombo.crawler.service.PlayerClient
import com.example.kbocombo.crawler.service.PlayerCrawler
import com.example.kbocombo.domain.player.Player
import org.springframework.stereotype.Component

@Component
class KboPlayerCrawler(
    private val kboPlayerDetailPageParser: KboPlayerDetailPageParser,
    private val playerCrawler: PlayerCrawler
) : PlayerClient {

    override fun findAllNewPlayers(savedPlayers: List<Player>): List<Player> {
        val players = playerCrawler.getPlayers()
        val savedWebIds = savedPlayers.map { it.webId }.toSet()
        val newPlayersData = players.filter { it.webId !in savedWebIds }
        return kboPlayerDetailPageParser.getPlayerProfile(newPlayersData)
    }
}
