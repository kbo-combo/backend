package com.example.kbocombo.crawler.game.application

import com.example.kbocombo.crawler.game.infra.dto.HitterRecordDto


interface HitterRecordClient {

    fun findAll(gameId: Long): List<HitterRecordDto>
}
