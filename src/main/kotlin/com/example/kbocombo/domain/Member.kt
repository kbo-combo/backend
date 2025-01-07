package com.example.kbocombo.domain

import com.example.kbocombo.domain.vo.SocialProvider
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "MEMBER")
@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "nickname", nullable = false)
    val nickname: String,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_provider")
    val socialProvider: SocialProvider?,

    @Column(name = "social_id")
    val socialId: String?
)
