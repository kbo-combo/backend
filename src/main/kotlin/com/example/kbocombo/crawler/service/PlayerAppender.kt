package com.example.kbocombo.crawler.service

import org.springframework.stereotype.Component

@Component
class PlayerAppender(
    private val playerClient: PlayerClient,
    private val playerRepository: PlayerRepository,
) {

    fun saveNewPlayers() {
        val savedPlayers = playerRepository.findAll()
        val originPlayers = playerClient.findAllPlayers()
        originPlayers.forEach(playerRepository::save)
    }
}
