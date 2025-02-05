package com.example.kbocombo.auth.application

import com.example.kbocombo.auth.domain.MemberSession
import com.example.kbocombo.auth.infra.MemberSessionRepository
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.infra.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class MemberSessionService(
    private val sessionRepository: MemberSessionRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun saveSession(memberId: Long, now: LocalDateTime): MemberSessionResponse {
        val memberSession = sessionRepository.save(
            MemberSession(
                memberId = memberId,
                now = now
            )
        )
        return MemberSessionResponse(sessionKey = memberSession.sessionKey)
    }

    @Transactional
    fun findMemberOrDeleteSession(sessionKey: String, now: LocalDateTime): Member? {
        val memberSession = sessionRepository.findBySessionKey(sessionKey) ?: return null
        if (memberSession.isExpired(now)) {
            sessionRepository.delete(memberSession)
            return null
        }

        return memberRepository.findById(memberSession.memberId)
    }
}

data class MemberSessionResponse(
    val sessionKey: String
)
