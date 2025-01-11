package com.example.kbocombo.auth.infra

import com.example.kbocombo.auth.domain.MemberInfo
import com.example.kbocombo.member.domain.vo.SocialProvider
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

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

@HttpExchange
interface KakaoAuthorizedHttpClient {

    @GetExchange(
        url = "/v2/user/me",
    )
    fun getMemberInfo(
        @RequestHeader("Authorization") authorization: String
    ): KakaoProfileResponse
}
