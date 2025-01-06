package com.example.kbocombo.crawler.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import java.util.Collections

@Configuration
class KboClientConfig {

    @Bean
    fun kboClient() : KboClient {
        val client = RestClient.builder()
            .baseUrl("https://www.koreabaseball.com")
            .defaultHeaders { it.addAll(getHeaders())}
            .build()
        val adapter = RestClientAdapter.create(client)
        val factory = HttpServiceProxyFactory.builderFor(adapter)
            .build()
        return factory.createClient(KboClient::class.java)
    }

    fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.accept = Collections.singletonList(MediaType.ALL)
        headers.add("Cache-Control", "no-cache")
        headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
        return headers
    }
}
