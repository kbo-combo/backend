package com.example.kbocombo.auth.infra

import com.example.kbocombo.auth.domain.MemberSession
import org.springframework.data.repository.Repository

interface MemberSessionRepository : Repository<MemberSession, Long> {

    fun save(memberSession: MemberSession): MemberSession

    fun findBySessionKey(sessionKey: String): MemberSession?

    fun delete(memberSession: MemberSession)
}
