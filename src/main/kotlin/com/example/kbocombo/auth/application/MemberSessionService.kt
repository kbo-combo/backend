package com.example.kbocombo.auth.application

import com.example.kbocombo.member.domain.Member
import java.time.LocalDateTime

interface MemberSessionService {

    fun saveSession(memberId: Long, now: LocalDateTime): MemberSessionResponse

    fun findMemberBySessionKey(sessionKey: String, now: LocalDateTime): Member?
}

data class MemberSessionResponse(
    val sessionKey: String
)
