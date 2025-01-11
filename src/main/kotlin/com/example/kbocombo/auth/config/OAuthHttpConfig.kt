package com.example.kbocombo.auth.config

import com.example.kbocombo.auth.infra.KakaoHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class OAuthHttpConfig {

    @Bean
    fun kakaoHttpClient(): KakaoHttpClient {
        val client = RestClient.builder()
            .baseUrl("https://kauth.kakao.com")
            .build()
        val adapter = RestClientAdapter.create(client)
        val factory = HttpServiceProxyFactory.builderFor(adapter)
            .build()
        return factory.createClient(KakaoHttpClient::class.java)
    }
}
