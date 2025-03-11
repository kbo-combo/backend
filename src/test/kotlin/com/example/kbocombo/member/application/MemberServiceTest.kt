package com.example.kbocombo.member.application

import com.example.kbocombo.annotation.IntegrationTest
import com.example.kbocombo.exception.type.BadRequestException
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.domain.vo.SocialProvider
import com.example.kbocombo.member.infra.MemberRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

@IntegrationTest
class MemberServiceTest(
    private val memberRepository: MemberRepository,
    private val memberService: MemberService
) : ExpectSpec({

    context("회원 정보 변경") {
        expect("닉네임을 변경한다.") {
            val member = Member(
                email = "kbocombo@example.com",
                nickname = "추강대엽",
                socialProvider = SocialProvider.KAKAO,
                socialId = "525245241"
            )
            val savedMember = memberRepository.save(member)

            val updateNickname = "이멤버리멤버포에버"
            memberService.updateNickname(memberId = savedMember.id, nickname = updateNickname)

            val foundMember = memberRepository.findById(savedMember.id)
            foundMember.nickname shouldBe updateNickname
        }

        expect("중복된 닉네임이면 예외가 발생한다.") {
            val duplicateNameMember = memberRepository.save(
                Member(
                    email = "kbocombo@example.com",
                    nickname = "duplicateNickname",
                    socialProvider = SocialProvider.KAKAO,
                    socialId = "456"
                )
            )
            val savedMember = memberRepository.save(
                Member(
                    email = "kbocombo@example.co.kr",
                    nickname = "originMember",
                    socialProvider = SocialProvider.KAKAO,
                    socialId = "123"
                )
            )

            val updateNickname = duplicateNameMember.nickname

            val exception = shouldThrow<BadRequestException> {
                memberService.updateNickname(memberId = savedMember.id, nickname = updateNickname)
            }

            exception.message shouldContain "중복된 닉네임"
        }
    }
})
