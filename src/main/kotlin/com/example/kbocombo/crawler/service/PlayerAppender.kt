package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.infrastructure.KboPlayerDetailPageParser
import org.springframework.stereotype.Component

@Component
class PlayerAppender(
    private val playerCrawler: PlayerCrawler,
    private val playerRepository: PlayerRepository,
    private val kboPlayerDetailPageParser: KboPlayerDetailPageParser
) {

    fun saveNewPlayers() {
        val totalPlayers = playerCrawler.getPlayers()
        val savedWebIds = playerRepository.findAll().map { it.webId }.toSet()
        val newPlayersData = totalPlayers.filter { it.webId !in savedWebIds }
        val newPlayers = kboPlayerDetailPageParser.getPlayerProfile(newPlayersData)
        newPlayers.forEach(playerRepository::save)
    }
}
