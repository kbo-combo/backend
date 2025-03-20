package com.example.kbocombo.config

import com.redis.testcontainers.RedisContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class RedisTestContainerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun redisContainer(): RedisContainer {
        return RedisContainer(DockerImageName.parse("redis:7.2.4"))
    }

    @Bean
    @Primary
    @DependsOn("redisContainer")
    fun testRedisConnectionFactory(redisContainer: RedisContainer): RedisConnectionFactory {
        return LettuceConnectionFactory(redisContainer.host, redisContainer.firstMappedPort)
    }

    @Bean
    @Primary
    @DependsOn("testRedisConnectionFactory")
    fun testRedisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            this.connectionFactory = connectionFactory
            this.keySerializer = StringRedisSerializer()
            this.valueSerializer = StringRedisSerializer()
            this.hashKeySerializer = StringRedisSerializer()
            this.hashValueSerializer = StringRedisSerializer()
            afterPropertiesSet()
        }
    }
}
