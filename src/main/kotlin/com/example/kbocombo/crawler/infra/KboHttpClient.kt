package com.example.kbocombo.crawler.infra

import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange
interface KboHttpClient {

    @PostExchange("/Player/Search.aspx")
    fun getPlayers(@RequestBody form: MultiValueMap<String, String>): ResponseEntity<String>
}
