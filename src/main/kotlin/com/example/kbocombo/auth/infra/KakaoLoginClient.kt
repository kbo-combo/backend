package com.example.kbocombo.auth.infra

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.nio.charset.StandardCharsets

@Component
class KakaoLoginClient(
    @Value("\${kakao.clientId}")
    private val clientId: String
) {
    private val restTemplate: RestTemplate = RestTemplate()

    fun createRedirectUri(redirectUrl: String): String {
        return UriComponentsBuilder.fromUriString(AUTHORIZE_REQUEST_URL)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUrl)
            .queryParam("response_type", RESPONSE_TYPE)
            .build()
            .toUriString()
    }

    fun getAccessToken(authCode: String, redirectUri: String): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            acceptCharset = listOf(StandardCharsets.UTF_8)
        }

        val body = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", GRANT_TYPE)
            add("client_id", clientId)
            add("redirect_uri", redirectUri)
            add("code", authCode)
        }

        val requestEntity = HttpEntity(body, headers)

        val responseEntity = restTemplate.exchange(
            ACCESS_TOKEN_REQUEST_URL,
            HttpMethod.POST,
            requestEntity,
            KakaoTokenResponse::class.java
        )

        val kakaoTokenResponse = responseEntity.body
            ?: throw IllegalStateException("카카오 로그인 토큰 발급에 실패했습니다.")

        return kakaoTokenResponse.accessToken
    }

    companion object {
        private const val AUTHORIZE_REQUEST_URL: String = "https://kauth.kakao.com/oauth/authorize"
        private const val ACCESS_TOKEN_REQUEST_URL: String = "https://kauth.kakao.com/oauth/token"
        private const val GRANT_TYPE: String = "authorization_code"
        private const val RESPONSE_TYPE: String = "code"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String
)
