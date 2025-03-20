package com.example.kbocombo.member.application

import com.example.kbocombo.member.application.response.MemberDetailResponse
import com.example.kbocombo.member.infra.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberQueryService(
    private val memberRepository: MemberRepository
) {

    fun getMemberDetail(memberId: Long): MemberDetailResponse {
        val member = memberRepository.findById(memberId)
        return MemberDetailResponse.from(member)
    }
}
