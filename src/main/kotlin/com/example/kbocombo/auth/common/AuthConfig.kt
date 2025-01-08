package com.example.kbocombo.auth.common

import com.example.kbocombo.auth.infra.OAuthClient
import com.example.kbocombo.auth.infra.OAuthClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfig {

    @Bean
    fun oauthClients(clients: Set<OAuthClient>): OAuthClients {
        return OAuthClients(oAuthClients = clients)
    }
}
