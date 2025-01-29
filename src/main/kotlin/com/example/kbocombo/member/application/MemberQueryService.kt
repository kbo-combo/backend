package com.example.kbocombo.member.application

import com.example.kbocombo.member.infra.MemberRepository
import com.example.kbocombo.member.ui.response.MemberDetailResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberQueryService(
    private val memberRepository: MemberRepository
) {

    fun getMemberDetail(memberId: Long) : MemberDetailResponse {
        val member = memberRepository.findById(memberId)
        return MemberDetailResponse.from(member)
    }
}
