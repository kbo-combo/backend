package com.example.kbocombo.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("http://localhost:*")
            .allowedMethods(*getAllowedMethods())
            .allowCredentials(true)
    }

    private fun getAllowedMethods() = HttpMethod.values()
        .map { it.name() }
        .toTypedArray()
}
