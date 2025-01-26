package com.example.kbocombo.member.application

import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.infra.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun updateNickname(memberId: Long, nickname: String) {
        val member = memberRepository.findById(memberId)

        val updateMember = Member(
            id = member.id,
            email = member.email,
            socialProvider = member.socialProvider,
            socialId = member.socialId,
            nickname = nickname,
        )
        memberRepository.save(updateMember)
    }
}
