package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.dto.PlayerResponse

interface PlayerClient {

    fun findAllPlayers() : List<PlayerResponse>
}
