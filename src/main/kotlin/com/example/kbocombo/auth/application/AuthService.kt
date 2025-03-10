package com.example.kbocombo.auth.application

import com.example.kbocombo.auth.domain.MemberInfo
import com.example.kbocombo.auth.infra.OAuthClients
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.domain.vo.SocialProvider
import com.example.kbocombo.member.infra.MemberRepository
import com.example.kbocombo.utils.UserRandomNameGenerator
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val oAuthClients: OAuthClients,
    private val userRandomNameGenerator: UserRandomNameGenerator,
    private val memberRepository: MemberRepository,
    private val publisher: ApplicationEventPublisher
) {

    fun getRedirectUri(socialProvider: SocialProvider, redirectUri: String): OAuthRedirectUriResponse {
        val authorizedRedirectUri = oAuthClients.getRedirectUri(
            socialProvider = socialProvider,
            redirectUri = redirectUri
        )
        return OAuthRedirectUriResponse(redirectUri = authorizedRedirectUri)
    }

    @Transactional
    fun login(
        socialProvider: SocialProvider,
        code: String,
        redirectUri: String
    ): OAuthMemberResponse {
        val memberInfo = oAuthClients.getMemberInfo(
            socialProvider = socialProvider,
            code = code,
            redirectUri = redirectUri
        )

        val member = findMemberOrSignup(socialProvider, memberInfo)
        return OAuthMemberResponse.from(member)
    }

    private fun findMemberOrSignup(
        socialProvider: SocialProvider,
        memberInfo: MemberInfo
    ): Member {
        val email = memberInfo.email

        return memberRepository.findByEmail(email) ?: signup(email, socialProvider, memberInfo)
    }

    private fun signup(
        email: String,
        socialProvider: SocialProvider,
        memberInfo: MemberInfo
    ): Member {
        val savedMember = memberRepository.save(
            Member(
                email = email,
                nickname = userRandomNameGenerator.generate(),
                socialProvider = socialProvider,
                socialId = memberInfo.socialId.toString()
            )
        )

        publisher.publishEvent(MemberSignupedEvent(memberId = savedMember.id))
        return savedMember
    }
}

data class OAuthRedirectUriResponse(
    val redirectUri: String
)

data class OAuthMemberResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val socialId: String?,
    val socialProvider: String?
) {
    companion object {
        fun from(member: Member): OAuthMemberResponse {
            return OAuthMemberResponse(
                id = member.id,
                email = member.email,
                nickname = member.nickname,
                socialId = member.socialId,
                socialProvider = member.socialProvider?.name
            )
        }
    }
}

data class MemberSignupedEvent(
    val memberId: Long
)
