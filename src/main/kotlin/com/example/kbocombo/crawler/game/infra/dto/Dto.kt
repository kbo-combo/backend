package com.example.kbocombo.crawler.game.infra.dto

import com.example.kbocombo.player.vo.WebId

data class HitterRecordDto(
    val webId: WebId,
    val atBats: Int,
    val hits: Int
)
