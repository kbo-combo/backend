package com.example.kbocombo.auth.domain

import com.example.kbocombo.member.domain.vo.SocialProvider

data class MemberInfo(
    val userId: Long,
    val nickname: String,
    val email: String,
    val socialProvider: SocialProvider,
)
