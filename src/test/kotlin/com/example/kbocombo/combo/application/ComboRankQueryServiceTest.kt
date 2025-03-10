package com.example.kbocombo.combo.application

import com.example.kbocombo.annotation.IntegrationTest
import com.example.kbocombo.combo.domain.ComboRank
import com.example.kbocombo.combo.infra.ComboRankRepository
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.domain.vo.SocialProvider
import com.example.kbocombo.member.infra.MemberRepository
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

@IntegrationTest
class ComboRankQueryServiceTest(
    private val comboRankQueryService: ComboRankQueryService,
    private val comboRankRepository: ComboRankRepository,
    private val memberRepository: MemberRepository
) : ExpectSpec({

    val comboYear = LocalDate.now().year

    context("콤보 랭크 기록") {
        expect("최초로 생성되는 엔티티에는 기록이 초기화 되어 있다.") {
            val member = memberRepository.save(getMember().sample())
            val comboRank = ComboRank.init(memberId = member.id, years = comboYear)

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
    }

    context("콤보 랭크 통계 조회") {
        expect("상위 N명을 조회하고 동점자 처리를 통해 순위를 결정한다.") {
            val memberA = memberRepository.save(getMember().sample())
            val memberB = memberRepository.save(getMember().sample())
            val memberC = memberRepository.save(getMember().sample())
            val comboRankA = ComboRank.init(memberId = memberA.id, years = comboYear)
            val comboRankB = ComboRank.init(memberId = memberB.id, years = comboYear)
            val comboRankC = ComboRank.init(memberId = memberC.id, years = comboYear)
            comboRankB.recordComboSuccess(gameDate = LocalDate.now())
            comboRankC.recordComboSuccess(gameDate = LocalDate.now())
            comboRankRepository.saveAll(listOf(comboRankA, comboRankB, comboRankC)) // memberB, memberC 동률

            val comboRankStatistic = comboRankQueryService.getComboRankStatistic(year = comboYear, count = 3)

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
