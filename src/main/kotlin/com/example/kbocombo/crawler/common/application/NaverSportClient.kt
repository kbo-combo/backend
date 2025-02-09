package com.example.kbocombo.crawler.common.application

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import java.time.LocalDate

@HttpExchange
interface NaverSportClient {

    @GetExchange(
        url = "/schedule/games/{gameCode}/record"
    )
    fun getLiveGameRecord(
        @PathVariable("gameCode") gameCode: String
    ): String

    @GetExchange(
        url = "/schedule/games"
    )
    fun getGameListByDate(
        @RequestParam upperCategoryId: String,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) fromDate: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) toDate: LocalDate,
        @RequestParam size: Int
    ): String

    @GetExchange(
        url = "/schedule/games/{gameCode}/preview"
    )
    fun getGamePreview(
        @PathVariable gameCode: String
    ): String
}
