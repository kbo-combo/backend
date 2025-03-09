package com.example.kbocombo.crawler.game.application

import com.example.kbocombo.record.application.HitterRecordDto

interface HitterRecordClient {

    fun findAll(gameId: Long): List<HitterRecordDto>
}
