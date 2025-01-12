package com.example.kbocombo.member.infra

import com.example.kbocombo.member.domain.Member
import org.springframework.data.repository.Repository

interface MemberRepository : Repository<Member, Long> {

    fun save(member: Member): Member

    fun findByEmail(email: String): Member?
}
