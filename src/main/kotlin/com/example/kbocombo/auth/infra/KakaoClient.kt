package com.example.kbocombo.auth.infra

import com.example.kbocombo.auth.domain.MemberInfo
import com.example.kbocombo.member.domain.vo.SocialProvider
import org.springframework.stereotype.Component

@Component
class KakaoClient(
    private val kakaoLoginClient: KakaoLoginClient,
    private val kakaoMemberInfoClient: KakaoMemberInfoClient
) : OAuthClient {

    override fun createRedirectUri(redirectUri: String): String {
        return kakaoLoginClient.createRedirectUri(redirectUrl = redirectUri)
    }

    override fun requestToken(code: String, redirectUri: String): String {
        return kakaoLoginClient.getAccessToken(authCode = code, redirectUri = redirectUri)
    }

    override fun findUserInfo(accessToken: String): MemberInfo {
        return kakaoMemberInfoClient.getMemberInfo(accessToken)
    }

    override fun getSocialProvider(): SocialProvider {
        return SocialProvider.KAKAO
    }
}
