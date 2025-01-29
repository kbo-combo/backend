package com.example.kbocombo.member.ui.response

import com.example.kbocombo.member.domain.Member

data class MemberDetailResponse(
    val memberId: Long,
    val nickname: String,
) {

    companion object {
        fun from(member: Member): MemberDetailResponse {
            return MemberDetailResponse(
                memberId = member.id,
                nickname = member.nickname,
            )
        }
    }
}
