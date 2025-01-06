package com.example.kbocombo.crawler.client

import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange
interface KboClient {

    @PostExchange("/Player/Register.aspx")
    fun getRegisteredPlayers(
        @RequestBody form: MultiValueMap<String, String>
    ): ResponseEntity<String>
}
