package com.example.kbocombo.member.application

import com.example.kbocombo.exception.type.BadRequestException
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
        checkDuplicate(nickname)

        val updateMember = Member(
            id = member.id,
            email = member.email,
            socialProvider = member.socialProvider,
            socialId = member.socialId,
            nickname = nickname,
        )
        memberRepository.save(updateMember)
    }

    private fun checkDuplicate(nickname: String) {
        if (memberRepository.existsByNickname(nickname)) {
            throw BadRequestException("{$nickname}은(는) 중복된 닉네임입니다.")
        }
    }
}
