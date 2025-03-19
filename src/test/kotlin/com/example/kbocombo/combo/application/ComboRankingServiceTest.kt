package com.example.kbocombo.combo.application

import com.example.kbocombo.annotation.IntegrationTest
import com.example.kbocombo.combo.domain.ComboRankingKey
import com.example.kbocombo.combo.infra.ComboRankingRepository
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDate

@IntegrationTest
class ComboRankingServiceTest(
    private val comboRankingService: ComboRankingService,
    private val comboRankingRepository: ComboRankingRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) : ExpectSpec({
    context("실시간 콤보 랭킹") {

        afterTest {
            val keys = redisTemplate.keys("${ComboRankingKey.playerComboRankByDate(LocalDate.now())}*")
            if (keys.isNotEmpty()) {
                redisTemplate.delete(keys)
            }
        }

        expect("특정 날짜에 선수 콤보 투표 증가 테스트") {
            // given
            val gameDate = LocalDate.now()
            val playerId = 1L

            // when
            comboRankingService.incrementPlayerComboVote(gameDate, playerId)

            // then
            val score = comboRankingRepository.getPlayerComboVoteCount(gameDate, playerId)
            score shouldBe 1L
        }

        expect("같은 선수에 대한 여러 투표 테스트") {
            // given
            val gameDate = LocalDate.now()
            val playerId = 1L

            // when
            repeat(5) {
                comboRankingService.incrementPlayerComboVote(gameDate, playerId)
            }

            // then
            val score = comboRankingRepository.getPlayerComboVoteCount(gameDate, playerId)
            score shouldBe 5L
        }

        expect("여러 선수에 대한 투표 및 랭킹 테스트") {
            // given
            val gameDate = LocalDate.now()
            val player1Id = 1L
            val player2Id = 2L
            val player3Id = 3L

            // when
            repeat(5) { comboRankingService.incrementPlayerComboVote(gameDate, player1Id) }
            repeat(10) { comboRankingService.incrementPlayerComboVote(gameDate, player2Id) }
            repeat(3) { comboRankingService.incrementPlayerComboVote(gameDate, player3Id) }

            // then
            val topPlayers = comboRankingRepository.getTopRankedPlayersByDate(gameDate, 3)

            topPlayers.size shouldBe 3
            topPlayers[0].first shouldBe player2Id.toString()
            topPlayers[0].second shouldBe 10L

            topPlayers[1].first shouldBe player1Id.toString()
            topPlayers[1].second shouldBe 5L

            topPlayers[2].first shouldBe player3Id.toString()
            topPlayers[2].second shouldBe 3L

            comboRankingRepository.getPlayerRankByDate(gameDate, player2Id) shouldBe 1L
            comboRankingRepository.getPlayerRankByDate(gameDate, player1Id) shouldBe 2L
            comboRankingRepository.getPlayerRankByDate(gameDate, player3Id) shouldBe 3L
        }
    }
})
