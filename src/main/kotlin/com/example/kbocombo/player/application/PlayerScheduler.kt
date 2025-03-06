package com.example.kbocombo.player.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.crawler.player.application.PlayerSyncService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PlayerScheduler(
    private val playerSyncService: PlayerSyncService
) {

    @Scheduled(cron = "0 0 5 * * ?")
    fun schedulePlayerData() {
        playerSyncService.syncAllPlayerData()
        logInfo("sync player data")
    }
}
