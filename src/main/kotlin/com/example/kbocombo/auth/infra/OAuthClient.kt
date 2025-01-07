package com.example.kbocombo.auth.infra

import com.example.kbocombo.auth.domain.MemberInfo
import com.example.kbocombo.domain.vo.SocialProvider

interface OAuthClient {

    fun createRedirectUri(redirectUri: String): String

    fun requestToken(code: String, redirectUri: String): String

    fun findUserInfo(accessToken: String): MemberInfo

    fun getSocialProvider(): SocialProvider
}
