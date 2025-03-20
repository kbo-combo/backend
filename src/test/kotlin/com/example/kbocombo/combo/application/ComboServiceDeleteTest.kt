package com.example.kbocombo.combo.application

import com.example.kbocombo.annotation.IntegrationTest
import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.domain.ComboVoteRankingKey
import com.example.kbocombo.combo.infra.ComboVoteRankingRepository
import com.example.kbocombo.combo.infra.ComboRepository
import com.example.kbocombo.config.RedisTestContainerConfig
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDate
import java.time.LocalDateTime

@IntegrationTest
@Import(RedisTestContainerConfig::class)
class ComboServiceDeleteTest(
    private val comboService: ComboService,
    private val comboRepository: ComboRepository,
    private val comboVoteRankingRepository: ComboVoteRankingRepository,
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) : ExpectSpec({

    beforeEach {
        // Redis의 모든 데이터 초기화
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushDb()
    }

    context("콤보 삭제") {
        val gameStartDateTime = LocalDateTime.now().plusDays(1)
        val comboCreatedDateTime = LocalDateTime.now().minusHours(1)

        expect("콤보 삭제 시 해당 선수의 콤보 랭킹이 감소한다") {
            val player = playerRepository.save(getPlayer().sample())
            val game = gameRepository.save(
                getGame(
                    gameState = GameState.PENDING,
                    gameStartDateTime = gameStartDateTime
                ).sample()
            )
            val combo = comboRepository.save(
                Combo(
                    game = game,
                    memberId = 1L,
                    playerId = player.id,
                    now = comboCreatedDateTime
                )
            )
            
            comboVoteRankingRepository.incrementPlayerComboVote(
                gameDate = game.startDate,
                playerId = player.id,
                increment = 1L
            )
            val beforeVoteCount = comboVoteRankingRepository.getPlayerComboVoteCount(game.startDate, player.id)

            comboService.deleteCombo(combo.id, comboCreatedDateTime.plusMinutes(1))
            
            val afterVoteCount = comboVoteRankingRepository.getPlayerComboVoteCount(game.startDate, player.id)
            beforeVoteCount shouldBe 1L
            afterVoteCount shouldBe 0L
        }
        
        expect("여러 개의 투표를 받은 선수의 콤보를 삭제해도 랭킹이 1만 감소한다") {
            val player = playerRepository.save(getPlayer().sample())
            val game = gameRepository.save(
                getGame(
                    gameState = GameState.PENDING,
                    gameStartDateTime = gameStartDateTime
                ).sample()
            )
            val combo = comboRepository.save(
                Combo(
                    game = game,
                    memberId = 1L,
                    playerId = player.id,
                    now = comboCreatedDateTime
                )
            )
            comboVoteRankingRepository.incrementPlayerComboVote(
                gameDate = game.startDate,
                playerId = player.id,
                increment = 3L
            )
            val beforeVoteCount = comboVoteRankingRepository.getPlayerComboVoteCount(game.startDate, player.id)
            
            comboService.deleteCombo(combo.id, comboCreatedDateTime.plusMinutes(1))
            
            val afterVoteCount = comboVoteRankingRepository.getPlayerComboVoteCount(game.startDate, player.id)
            beforeVoteCount shouldBe 3L
            afterVoteCount shouldBe 2L
        }
    }
})

private fun getPlayer() =
    fixture.giveMeKotlinBuilder<Player>()
        .setExp(Player::id, 0L)
        .setExp(Player::isRetired, false)

private fun getGame(
    gameState: GameState? = null,
    gameStartDateTime: LocalDateTime
) = fixture.giveMeKotlinBuilder<Game>()
    .setExp(Game::id, 0L)
    .setExp(Game::gameType, GameType.REGULAR_SEASON)
    .setExp(Game::gameState, gameState ?: GameState.RUNNING)
    .setExp(Game::startDate, gameStartDateTime.toLocalDate())
    .setExp(Game::startTime, gameStartDateTime.toLocalTime()) 
