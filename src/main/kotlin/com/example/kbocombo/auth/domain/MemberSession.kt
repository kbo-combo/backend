package com.example.kbocombo.auth.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID

@Entity(name = "MEMBER_SESSION")
class MemberSession private constructor(
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

    constructor(
        memberId: Long,
        now: LocalDateTime
    ) : this(
        memberId = memberId,
        sessionKey = UUID.randomUUID().toString(),
        expiredDateTime = now.plusDays(SESSION_EXPIRED_DAY),
    )

    fun isExpired(now: LocalDateTime): Boolean {
        return expiredDateTime.isBefore(now)
    }

    fun extendIfEnable(now: LocalDateTime)  {
        if (expiredDateTime.isBefore(now.plusDays(SESSION_EXTEND_DAY_GAP))) {
            expiredDateTime = now.plusDays(SESSION_EXPIRED_DAY)
        }
    }

    companion object {
        private const val SESSION_EXTEND_DAY_GAP = 1L
        private const val SESSION_EXPIRED_DAY = 7L
    }
}
