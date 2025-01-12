package com.example.kbocombo.crawler.config

import com.example.kbocombo.crawler.infra.KboHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class KboHttpClientConfig {

    @Bean
    fun kboClient() : KboHttpClient {
        val client = RestClient.builder()
            .baseUrl("https://www.koreabaseball.com")
            .build()
        val adapter = RestClientAdapter.create(client)
        val factory = HttpServiceProxyFactory.builderFor(adapter)
            .build()
        return factory.createClient(KboHttpClient::class.java)
    }
}
