package com.example.kbocombo.auth.infra

import com.example.kbocombo.auth.domain.UserInfo
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class KakaoLoginUserInfoClient {

    fun request(accessToken: String): UserInfo {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $accessToken")
        }

        val requestEntity = HttpEntity<Void>(headers)

        val responseEntity = restTemplate.exchange(
            PROFILE_REQUEST_URL,
            HttpMethod.GET,
            requestEntity,
            KakaoProfileResponse::class.java
        )

        val kakaoProfileResponse = responseEntity.body
            ?: throw IllegalStateException("카카오 유저 정보 가져오기를 실패했습니다.")

        return kakaoProfileResponse.toUserInfo()
    }

    companion object {
        private const val PROFILE_REQUEST_URL = "https://kapi.kakao.com/v2/user/me"
    }
}

data class KakaoProfileResponse(
    val userId: Long,
    val nickname: String,
) {
    fun toUserInfo(): UserInfo {
        return UserInfo(
            userId = userId,
            nickname = nickname,
        )
    }
}
