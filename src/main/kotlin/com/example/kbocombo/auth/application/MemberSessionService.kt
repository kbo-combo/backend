package com.example.kbocombo.auth.application

import com.example.kbocombo.auth.infra.MemberSessionRepository
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.infra.MemberRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MemberSessionService(
    private val sessionRepository: MemberSessionRepository,
    private val memberRepository: MemberRepository
) {

    fun findMemberBySessionKey(sessionKey: String, now: LocalDateTime): Member? {
        val memberSession = sessionRepository.findBySessionKey(sessionKey)
        requireNotNull(memberSession)
        if (memberSession.isExpired(now)) {
            sessionRepository.delete(memberSession)
            return null
        }
        return memberRepository.findById(memberSession.memberId)
    }
}
