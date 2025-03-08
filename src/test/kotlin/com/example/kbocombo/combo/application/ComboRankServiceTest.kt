package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.ComboRank
import com.example.kbocombo.combo.infra.ComboRankRepository
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.domain.vo.SocialProvider
import com.example.kbocombo.member.infra.MemberRepository
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class ComboRankServiceTest(
    private val comboRankService: ComboRankService,
    private val comboRankRepository: ComboRankRepository,
    private val memberRepository: MemberRepository
) : ExpectSpec({

    context("콤보 랭크 기록") {
        expect("최초로 생성되는 엔티티에는 기록이 초기화 되어 있다.") {
            val member = memberRepository.save(getMember().sample())
            val comboRank = ComboRank.init(memberId = member.id)

            val savedComboRank = comboRankRepository.save(comboRank)

            savedComboRank.memberId shouldBe member.id
            savedComboRank.currentRecord shouldBe 0
            savedComboRank.totalCount shouldBe 0
            savedComboRank.successCount shouldBe 0
            savedComboRank.failCount shouldBe 0
            savedComboRank.passCount shouldBe 0
            savedComboRank.firstSuccessDate shouldBe null
            savedComboRank.lastSuccessDate shouldBe null
        }

        expect("콤보 성공을 기록하면 totalCount, successCount 값이 1 증가한다.") {
            val member = memberRepository.save(getMember().sample())
            val comboRank = ComboRank.init(memberId = member.id)
            val priorComboRank = comboRankRepository.save(comboRank)

            comboRankService.recordSuccess(memberId = member.id)

            val successAppliedComboRank = comboRankRepository.findByMemberId(memberId = member.id)
            successAppliedComboRank.memberId shouldBe member.id
            successAppliedComboRank.totalCount shouldBe priorComboRank.totalCount + 1
            successAppliedComboRank.successCount shouldBe priorComboRank.successCount + 1
            successAppliedComboRank.failCount shouldBe priorComboRank.failCount
            successAppliedComboRank.passCount shouldBe priorComboRank.passCount
            successAppliedComboRank.lastSuccessDate shouldBe LocalDate.now()
        }

        expect("콤보 실패를 기록하면 totalCount, failCount 값이 1 증가한다.") {
            val member = memberRepository.save(getMember().sample())
            val comboRank = ComboRank.init(memberId = member.id)
            val priorComboRank = comboRankRepository.save(comboRank)

            comboRankService.recordFail(memberId = member.id)

            val successAppliedComboRank = comboRankRepository.findByMemberId(memberId = member.id)
            successAppliedComboRank.memberId shouldBe member.id
            successAppliedComboRank.totalCount shouldBe priorComboRank.totalCount + 1
            successAppliedComboRank.failCount shouldBe priorComboRank.failCount + 1
            successAppliedComboRank.successCount shouldBe priorComboRank.successCount
            successAppliedComboRank.passCount shouldBe priorComboRank.passCount
            successAppliedComboRank.firstSuccessDate shouldBe null
            successAppliedComboRank.lastSuccessDate shouldBe null
        }

        expect("콤보 패스를 기록하면 totalCount, passCount 값이 1 증가한다.") {
            val member = memberRepository.save(getMember().sample())
            val comboRank = ComboRank.init(memberId = member.id)
            val priorComboRank = comboRankRepository.save(comboRank)

            comboRankService.recordPass(memberId = member.id)

            val successAppliedComboRank = comboRankRepository.findByMemberId(memberId = member.id)
            successAppliedComboRank.memberId shouldBe member.id
            successAppliedComboRank.totalCount shouldBe priorComboRank.totalCount + 1
            successAppliedComboRank.failCount shouldBe priorComboRank.failCount
            successAppliedComboRank.successCount shouldBe priorComboRank.successCount
            successAppliedComboRank.passCount shouldBe priorComboRank.passCount + 1
        }
    }

    context("콤보 랭크 통계 조회") {
        expect("상위 N명을 조회하고 동점자 처리를 통해 순위를 결정한다.") {
            val memberA = memberRepository.save(getMember().sample())
            val memberB = memberRepository.save(getMember().sample())
            val memberC = memberRepository.save(getMember().sample())
            val comboRankA = ComboRank.init(memberId = memberA.id)
            val comboRankB = ComboRank.init(memberId = memberB.id)
            val comboRankC = ComboRank.init(memberId = memberC.id)
            comboRankB.recordComboSuccess()
            comboRankC.recordComboSuccess()
            comboRankRepository.saveAll(listOf(comboRankA, comboRankB, comboRankC)) // memberB, memberC 동률

            val comboRankStatistic = comboRankService.getComboRankStatistic(3)

            comboRankStatistic.topRanks.size shouldBe 3
            comboRankStatistic.topRanks[0].rank shouldBe 1
            comboRankStatistic.topRanks[1].rank shouldBe 1
            comboRankStatistic.topRanks[2].rank shouldBe 3
        }
    }
})

private fun getMember() =
    fixture.giveMeKotlinBuilder<Member>()
        .setExp(Member::id, 0L)
        .setExp(Member::socialProvider, SocialProvider.KAKAO)
