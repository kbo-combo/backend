package com.example.kbocombo.combo.application

import com.example.kbocombo.annotation.IntegrationTest
import com.example.kbocombo.combo.domain.ComboVoteRankingKey
import com.example.kbocombo.combo.infra.ComboVoteRankingRepository
import com.example.kbocombo.config.RedisTestContainerConfig
import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDate

@IntegrationTest
@Import(RedisTestContainerConfig::class)
class ComboRankingServiceTest(
    private val comboVoteRankingService: ComboVoteRankingService,
    private val comboVoteRankingRepository: ComboVoteRankingRepository,
    private val playerRepository: PlayerRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) : ExpectSpec({

    beforeEach {
        // Redis의 모든 데이터 초기화
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushDb()
    }

    context("실시간 콤보 랭킹") {
        expect("특정 날짜에 선수 콤보 투표 증가 테스트") {
            // given
            val gameDate = LocalDate.now()
            val player = playerRepository.save(getPlayer().sample())

            // when
            comboVoteRankingService.incrementPlayerComboVote(gameDate, player.id)

            // then
            val score = comboVoteRankingRepository.getPlayerComboVoteCount(gameDate, player.id)
            score shouldBe 1L
        }

        expect("같은 선수에 대한 여러 투표 테스트") {
            // given
            val gameDate = LocalDate.now()
            val player = playerRepository.save(getPlayer().sample())

            // when
            repeat(5) {
                comboVoteRankingService.incrementPlayerComboVote(gameDate, player.id)
            }

            // then
            val score = comboVoteRankingRepository.getPlayerComboVoteCount(gameDate, player.id)
            score shouldBe 5L
        }

        expect("여러 선수에 대한 투표 및 랭킹 테스트") {
            // given
            val gameDate = LocalDate.now()
            val playerA = playerRepository.save(getPlayer().sample())
            val playerB = playerRepository.save(getPlayer().sample())
            val playerC = playerRepository.save(getPlayer().sample())

            // when
            repeat(5) { comboVoteRankingService.incrementPlayerComboVote(gameDate, playerA.id) }
            repeat(10) { comboVoteRankingService.incrementPlayerComboVote(gameDate, playerB.id) }
            repeat(3) { comboVoteRankingService.incrementPlayerComboVote(gameDate, playerC.id) }

            // then
            val topPlayers = comboVoteRankingRepository.getTopRankedPlayersByDate(gameDate, 3)

            topPlayers.size shouldBe 3
            topPlayers[0].first.toLong() shouldBe playerB.id
            topPlayers[0].second shouldBe 10L

            topPlayers[1].first.toLong() shouldBe playerA.id
            topPlayers[1].second shouldBe 5L

            topPlayers[2].first.toLong() shouldBe playerC.id
            topPlayers[2].second shouldBe 3L

            comboVoteRankingRepository.getPlayerRankByDate(gameDate, playerB.id) shouldBe 1L
            comboVoteRankingRepository.getPlayerRankByDate(gameDate, playerA.id) shouldBe 2L
            comboVoteRankingRepository.getPlayerRankByDate(gameDate, playerC.id) shouldBe 3L
        }
    }
})

private fun getPlayer() =
    fixture.giveMeKotlinBuilder<Player>()
        .setExp(Player::id, 0L)
        .setExp(Player::isRetired, false)
