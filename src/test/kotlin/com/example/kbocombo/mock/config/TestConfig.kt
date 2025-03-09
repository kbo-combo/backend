package com.example.kbocombo.mock.config

import com.example.kbocombo.mock.event.CustomDefaultApplicationEvents
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.event.ApplicationEvents

@TestConfiguration
class TestConfig {

    @Bean
    fun applicationEvents(): ApplicationEvents {
        return CustomDefaultApplicationEvents()
    }
}
