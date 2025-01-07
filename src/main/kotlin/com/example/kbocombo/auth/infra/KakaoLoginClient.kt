package com.example.kbocombo.auth.infra

import com.example.kbocombo.auth.domain.UserInfo
import java.nio.charset.StandardCharsets
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class KakaoLoginClient(
    @Value("\${kakao.clientId}")
    private val clientId: String,
) {

    fun createRedirectUrl(redirectUrl: String): String {
        return UriComponentsBuilder.fromUriString(AUTHORIZE_REQUEST_URL)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUrl)
            .queryParam("response_type", RESPONSE_TYPE)
            .build()
            .toUriString()
    }

    fun request(authCode: String, redirectUri: String): KakaoTokenResponse {
        val restTemplate = RestTemplate()
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
            TOKEN_REQUEST_URL,
            HttpMethod.POST,
            requestEntity,
            KakaoTokenResponse::class.java
        )

        val kakaoTokenResponse = responseEntity.body
            ?: throw IllegalStateException("카카오 로그인 토큰 발급에 실패했습니다.")

        return kakaoTokenResponse
    }

    companion object {
        private const val AUTHORIZE_REQUEST_URL: String = "https://kauth.kakao.com/oauth/authorize"
        private const val TOKEN_REQUEST_URL: String = "https://kauth.kakao.com/oauth/token"
        private const val GRANT_TYPE: String = "authorization_code"
        private const val RESPONSE_TYPE: String = "code"
    }
}

data class KakaoTokenResponse(
    val accessToken: String
)
