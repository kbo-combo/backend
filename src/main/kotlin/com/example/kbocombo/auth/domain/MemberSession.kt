package com.example.kbocombo.auth.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity(name = "MEMBER_SESSION")
class MemberSession(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "member_id", nullable = false, updatable = false)
    val memberId: Long,

    @Column(name = "session_key", nullable = false, updatable = false, unique = true)
    val sessionKey: String,

    @Column(name = "expired_datetime", nullable = false, updatable = false)
    var expiredDateTime: LocalDateTime,
 ) {

    fun isExpired(now: LocalDateTime): Boolean {
        return expiredDateTime.isBefore(now)
    }
}
