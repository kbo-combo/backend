package com.example.kbocombo.crawler.application

import org.springframework.stereotype.Service

@Service
class PlayerSyncService(
    private val playerClient: PlayerClient,
    private val playerRenewService: PlayerRenewService
) {

    fun syncAllPlayerData() {
        val recentPlayers = playerClient.getRecentPlayers()
        playerRenewService.renew(recentPlayers)
    }
}
