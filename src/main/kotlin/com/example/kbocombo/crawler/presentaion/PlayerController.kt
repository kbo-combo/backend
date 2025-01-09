package com.example.kbocombo.crawler.presentaion

import com.example.kbocombo.crawler.service.PlayerSyncService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/player")
class PlayerController(
    private val playerSyncService: PlayerSyncService
) {


    @GetMapping
    fun add() {
        playerSyncService.synchronizePlayers()
    }
}
