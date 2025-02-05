package com.example.kbocombo.mock.infra

import com.example.kbocombo.auth.domain.MemberSession
import com.example.kbocombo.auth.infra.MemberSessionRepository

class FakeMemberSessionRepository  : BaseFakeRepository<MemberSession>(MemberSession::class), MemberSessionRepository {

    override fun findBySessionKey(sessionKey: String): MemberSession? {
        return db.find { it.sessionKey == sessionKey }
    }

    override fun delete(memberSession: MemberSession) {
        db.remove(memberSession)
    }
}
