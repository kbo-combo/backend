package com.example.kbocombo.crawler.client

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange
interface KboClient {

    @PostExchange("/Player/Search.aspx")
    fun getPlayers(
        @RequestBody form: MultiValueMap<String, String>,
        @RequestHeader(HttpHeaders.COOKIE, required = false) cookie: String
    ): ResponseEntity<String>
}
