package com.example.kbocombo.auth.infra

import com.example.kbocombo.auth.domain.MemberInfo
import com.example.kbocombo.member.domain.vo.SocialProvider
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.stereotype.Component

@Component
class KakaoMemberInfoClient(
    private val kakaoAuthorizedHttpClient: KakaoAuthorizedHttpClient
) {

    fun getMemberInfo(accessToken: String): MemberInfo {
        val token = "Bearer $accessToken"
        val kakaoProfileResponse = kakaoAuthorizedHttpClient.getMemberInfo(authorization = token)

        return kakaoProfileResponse.toMemberInfo()
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoProfileResponse(
    @JsonProperty("id")
    val socialId: Long,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount,

    ) {
    fun toMemberInfo(): MemberInfo {
        return MemberInfo(
            socialId = socialId,
            socialProvider = SocialProvider.KAKAO,
            email = kakaoAccount.email,
        )
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoAccount(
    @JsonProperty("email")
    val email: String
)
