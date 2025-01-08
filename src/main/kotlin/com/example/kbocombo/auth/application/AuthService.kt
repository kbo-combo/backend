package com.example.kbocombo.auth.application

import com.example.kbocombo.auth.domain.MemberInfo
import com.example.kbocombo.auth.infra.OAuthClients
import com.example.kbocombo.member.domain.vo.SocialProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val oAuthClients: OAuthClients
) {

    fun getRedirectUri(socialProvider: SocialProvider, redirectUri: String): String {
        return oAuthClients.getRedirectUri(
            socialProvider = socialProvider,
            redirectUri = redirectUri
        )
    }

    fun getMemberInfo(
        socialProvider: SocialProvider,
        code: String,
        redirectUri: String
    ): MemberInfo {
        return oAuthClients.getMemberInfo(
            socialProvider = socialProvider,
            code = code,
            redirectUri = redirectUri
        )
    }
}
