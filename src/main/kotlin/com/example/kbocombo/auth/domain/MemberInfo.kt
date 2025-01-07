package com.example.kbocombo.auth.domain

import com.example.kbocombo.domain.vo.SocialProvider

data class MemberInfo(
    private val userId: Long,
    private val socialProvider: SocialProvider,
) {
}
