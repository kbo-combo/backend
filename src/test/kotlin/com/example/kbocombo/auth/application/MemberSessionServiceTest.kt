package com.example.kbocombo.auth.application

import com.example.kbocombo.auth.domain.MemberSession
import com.example.kbocombo.auth.infra.MemberSessionRepository
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.infra.MemberRepository
import com.example.kbocombo.mock.infra.FakeMemberRepository
import com.example.kbocombo.mock.infra.FakeMemberSessionRepository
import com.example.kbocombo.utils.fixture
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import java.time.LocalDate
import java.time.LocalDateTime

@DisplayNameGeneration(ReplaceUnderscores::class)
class MemberSessionServiceTest : StringSpec({

    lateinit var sut: MemberSessionService
    lateinit var memberRepository: MemberRepository
    lateinit var memberSessionRepository: MemberSessionRepository

    beforeTest {
        memberRepository = FakeMemberRepository()
        memberSessionRepository = FakeMemberSessionRepository()
        sut = MemberSessionService(memberSessionRepository, memberRepository)
    }

    "일치하는 세션이 없으면 null return" {
        val actual = sut.findMemberOrDeleteSession(sessionKey = "절대없음", LocalDateTime.now())

        actual shouldBe null
    }

    "세션이 만료됐으면 삭제하고 null return." {
        val today = LocalDateTime.now()
        val memberSession = memberSessionRepository.save(
            MemberSession(
                memberId = 1L,
                now = today.minusYears(10))
        )

        val actual = sut.findMemberOrDeleteSession(sessionKey = memberSession.sessionKey, today)

        memberSessionRepository.findBySessionKey(memberSession.sessionKey) shouldBe null
        actual shouldBe null

    }

    "세션이 만료 1일전이면 1주일 연장" {
        val memberSession = memberSessionRepository.save(
            MemberSession(
                memberId = 1L,
                now = LocalDateTime.parse("2025-05-03T14:00:00")
        ))

        sut.findMemberOrDeleteSession(sessionKey = memberSession.sessionKey,
            now = LocalDateTime.parse("2025-05-09T14:00:00"))

        memberSession.expiredDateTime.toLocalDate() shouldBe LocalDate.parse("2025-05-10")
    }

    "정상 조회 성공" {
        val now = LocalDateTime.parse("2025-05-03T14:00:00")
        val member = memberRepository.save(fixture.giveMeOne(Member::class.java))
        val memberSession = memberSessionRepository.save(
            MemberSession(memberId = member.id, now = now))

        var actual = sut.findMemberOrDeleteSession(sessionKey = memberSession.sessionKey, now = now)

        actual shouldBe member
    }
})
