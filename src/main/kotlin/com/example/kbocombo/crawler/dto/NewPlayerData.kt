package com.example.kbocombo.crawler.dto

import com.example.kbocombo.domain.player.vo.PlayerPosition
import com.example.kbocombo.domain.player.vo.Team
import com.example.kbocombo.domain.player.vo.WebId

class NewPlayerData(
    val webId: WebId,
    val position: PlayerPosition,
    val team: Team
)
