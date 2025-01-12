package com.example.kbocombo.auth.infra

import com.example.kbocombo.auth.domain.MemberInfo
import com.example.kbocombo.member.domain.vo.SocialProvider
import org.springframework.stereotype.Component

@Component
class OAuthClients(
    private val oAuthClients: Set<OAuthClient>
) {
    private val clients: Map<SocialProvider, OAuthClient> = oAuthClients.associateBy { it.getSocialProvider() }

    fun getRedirectUri(socialProvider: SocialProvider, redirectUri: String): String {
        val client = getClient(socialProvider)
        return client.createRedirectUri(redirectUri = redirectUri)
    }

    fun getMemberInfo(socialProvider: SocialProvider, code: String, redirectUri: String): MemberInfo {
        val client = getClient(socialProvider)
        val accessToken = client.requestToken(code, redirectUri)
        return client.findUserInfo(accessToken = accessToken)
    }

    private fun getClient(socialProvider: SocialProvider): OAuthClient {
        return clients[socialProvider]
            ?: throw IllegalArgumentException("${socialProvider}타입의 OAuth는 지원되지 않습니다.")
    }
}
