package com.example.kbocombo.auth

import com.example.kbocombo.auth.domain.UserInfo

interface OAuthClient {

    fun createRedirectUri(redirectUri: String): String

    fun requestToken(code: String, redirectUri: String): String

    fun findUserInfo(accessToken: String): UserInfo

    fun getSocialType(): String
}
