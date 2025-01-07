package com.example.kbocombo.auth.infra

import com.example.kbocombo.auth.domain.MemberInfo
import com.example.kbocombo.domain.vo.SocialProvider
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class KakaoUserInfoClient {

    fun getUserInfo(accessToken: String): MemberInfo {
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

        return kakaoProfileResponse.toMemberInfo()
    }

    companion object {
        private const val PROFILE_REQUEST_URL = "https://kapi.kakao.com/v2/user/me"
    }
}

data class KakaoProfileResponse(
    val userId: Long,
) {
    fun toMemberInfo(): MemberInfo {
        return MemberInfo(
            userId = userId,
            socialProvider = SocialProvider.KAKAO
        )
    }
}
