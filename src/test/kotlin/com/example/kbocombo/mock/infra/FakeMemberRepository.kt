package com.example.kbocombo.mock.infra

import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.infra.MemberRepository
import com.example.kbocombo.utils.fixture

class FakeMemberRepository : BaseFakeRepository<Member>(Member::class), MemberRepository {

    override fun findByEmail(email: String): Member? {
        return db.find { it.email == email }
    }

    override fun findById(id: Long): Member {
        return db.find { it.id == id } ?: fixture.giveMeOne(Member::class.java)
    }
}
