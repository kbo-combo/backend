package com.example.kbocombo.crawler.common.config

import com.example.kbocombo.crawler.common.application.NaverSportClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class NaverSportClientConfig {

    @Bean
    fun naverSportClient(): NaverSportClient {
        val client = RestClient.builder()
            .baseUrl("https://api-gw.sports.naver.com")
            .build()
        val adapter = RestClientAdapter.create(client)
        val factory = HttpServiceProxyFactory.builderFor(adapter)
            .build()
        return factory.createClient(NaverSportClient::class.java)
    }
}
