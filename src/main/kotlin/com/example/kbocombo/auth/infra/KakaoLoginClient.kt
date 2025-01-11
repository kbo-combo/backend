package com.example.kbocombo.auth.infra

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.util.UriComponentsBuilder

@Component
class KakaoLoginClient(
    @Value("\${kakao.client_id}")
    private val clientId: String,
    private val kakaoHttpClient: KakaoHttpClient
) {
    fun createRedirectUri(redirectUrl: String): String {
        return UriComponentsBuilder.fromUriString(AUTHORIZE_REQUEST_URL)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUrl)
            .queryParam("response_type", RESPONSE_TYPE)
            .build()
            .toUriString()
    }

    fun getAccessToken(authCode: String, redirectUri: String): String {
        val kakaoTokenResponse = kakaoHttpClient.getAccessToken(
            grantType = GRANT_TYPE,
            clientId = clientId,
            redirectUri = redirectUri,
            authCode = authCode
        )

        return kakaoTokenResponse.accessToken
    }

    companion object {
        private const val AUTHORIZE_REQUEST_URL: String = "https://kauth.kakao.com/oauth/authorize"
        private const val GRANT_TYPE: String = "authorization_code"
        private const val RESPONSE_TYPE: String = "code"
    }
}

@HttpExchange
interface KakaoHttpClient {

    @PostExchange(
        url = "/oauth/token",
        headers = [
            "Content-Type=application/x-www-form-urlencoded",
            "Accept-Charset=UTF-8"
        ]
    )
    fun getAccessToken(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("code") authCode: String
    ): KakaoTokenResponse
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String
)
