package com.example.kbocombo.crawler.infra

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange
interface KboHttpClient {

    @PostExchange(
        url = "/Player/Search.aspx",
        headers = [APPLICATION_FORM_URLENCODED, NO_CACHE, CHROME]
    )
    fun getPlayers(@RequestBody form: MultiValueMap<String, String>): ResponseEntity<String>

    companion object {
        private const val APPLICATION_FORM_URLENCODED = "${HttpHeaders.CONTENT_TYPE}=${MediaType.APPLICATION_FORM_URLENCODED_VALUE}"
        private const val NO_CACHE = "${HttpHeaders.CACHE_CONTROL}=no-cache"
        private const val CHROME = "${HttpHeaders.USER_AGENT}=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML; like Gecko) Chrome/125.0.0.0 Safari/537.36"
    }
}

