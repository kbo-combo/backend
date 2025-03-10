package com.example.kbocombo.combo.infra

import com.example.kbocombo.annotation.IntegrationTest
import com.example.kbocombo.combo.domain.ComboRank
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.domain.vo.SocialProvider
import com.example.kbocombo.member.infra.MemberRepository
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

@IntegrationTest
class ComboRankQueryRepositoryTest(
    private val comboRankRepository: ComboRankRepository,
    private val comboRankQueryRepository: ComboRankQueryRepository,
    private val memberRepository: MemberRepository
) : ExpectSpec({

    context("콤보 랭크 상위 N명 조회") {
        expect("상위 3명을 조회한다.") {
            val memberA = memberRepository.save(getMember().sample())
            val memberB = memberRepository.save(getMember().sample())
            val memberC = memberRepository.save(getMember().sample())
            val comboRankA = ComboRank.init(
                memberId = memberA.id,
                years = LocalDate.now().year,
                gameType = GameType.REGULAR_SEASON
            )
            val comboRankB = ComboRank.init(
                memberId = memberB.id,
                years = LocalDate.now().year,
                gameType = GameType.REGULAR_SEASON
            )
            val comboRankC = ComboRank.init(
                memberId = memberC.id,
                years = LocalDate.now().year,
                gameType = GameType.REGULAR_SEASON
            )
            comboRankB.recordComboSuccess(gameDate = LocalDate.now())
            comboRankC.recordComboSuccess(gameDate = LocalDate.now())
            comboRankC.recordComboSuccess(gameDate = LocalDate.now())
            comboRankRepository.saveAll(listOf(comboRankA, comboRankB, comboRankC))

            val findTopRanks = comboRankQueryRepository.findTopRanks(year = LocalDate.now().year, limit = 3)

            findTopRanks.size shouldBe 3
            findTopRanks[0].memberId shouldBe memberC.id
            findTopRanks[1].memberId shouldBe memberB.id
            findTopRanks[2].memberId shouldBe memberA.id
        }
    }
})

private fun getMember() =
    fixture.giveMeKotlinBuilder<Member>()
        .setExp(Member::id, 0L)
        .setExp(Member::socialProvider, SocialProvider.KAKAO)
