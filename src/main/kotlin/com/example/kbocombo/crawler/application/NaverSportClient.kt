package com.example.kbocombo.crawler.application

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface NaverSportClient {

    @GetExchange(
        url = "/schedule/games/{gameCode}/record"
    )
    fun getLiveGameRecord(
        @PathVariable("gameCode") gameCode: String
    ): String
}
