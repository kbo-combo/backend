package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.combo.infra.ComboRepository
import com.example.kbocombo.combo.infra.getById
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.domain.vo.SocialProvider
import com.example.kbocombo.member.infra.MemberRepository
import com.example.kbocombo.player.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ComboServiceTest(
    private val memberRepository: MemberRepository,
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val comboRepository: ComboRepository,
    private val comboService: ComboService,
) : ExpectSpec({

    val comboCreatedDateTime = LocalDateTime.of(2024, 8, 21, 22, 30)
    val gameStartDateTime = LocalDateTime.of(2024, 8, 22, 18, 30)

    context("콤보 업데이트") {
        expect("진행 중인 게임에서 PENDING 상태의 콤보를 SUCCESS 상태로 변경한다.") {
            val memberA = memberRepository.save(getMember().sample())
            val memberB = memberRepository.save(getMember().sample())
            val playerA = playerRepository.save(getPlayer().sample())
            val playerB = playerRepository.save(getPlayer().sample())
            val game = gameRepository.save(
                getGame(
                    gameState = GameState.RUNNING,
                    gameStartDateTime = gameStartDateTime
                ).sample()
            )
            val comboA = comboRepository.save(
                Combo(
                    game = game,
                    memberId = memberA.id,
                    playerId = playerA.id,
                    now = comboCreatedDateTime,
                )
            )
            val comboB = comboRepository.save(
                Combo(
                    game = game,
                    memberId = memberB.id,
                    playerId = playerB.id,
                    now = comboCreatedDateTime,
                )
            )

            comboService.updateComboToSuccess(gameId = game.id, playerWebId = playerA.webId.value)

            comboRepository.getById(comboA.id).comboStatus shouldBe ComboStatus.SUCCESS
            comboRepository.getById(comboB.id).comboStatus shouldBe ComboStatus.PENDING
        }

        expect("SUCCESS 상태로 처리할 때 진행 중인 게임이 아니면 예외가 발생한다.") {
            val player = playerRepository.save(getPlayer().sample())
            val game = gameRepository.save(
                getGame(
                    gameState = GameState.PENDING,
                    gameStartDateTime = gameStartDateTime
                ).sample()
            )

            shouldThrowWithMessage<IllegalArgumentException>("진행 중인 게임이 아닌 경우, 콤보 성공 처리를 할 수 없습니다.") {
                comboService.updateComboToSuccess(gameId = game.id, playerWebId = player.webId.value)
            }
        }

        expect("종료된 게임에서 PENDING 상태의 콤보를 FAIL 상태로 변경한다.") {
            val memberA = memberRepository.save(getMember().sample())
            val memberB = memberRepository.save(getMember().sample())
            val playerA = playerRepository.save(getPlayer().sample())
            val playerB = playerRepository.save(getPlayer().sample())
            val game = gameRepository.save(
                getGame(
                    gameState = GameState.COMPLETED,
                    gameStartDateTime = gameStartDateTime
                ).sample()
            )
            val comboA = comboRepository.save(
                Combo(
                    game = game,
                    memberId = memberA.id,
                    playerId = playerA.id,
                    now = comboCreatedDateTime,
                )
            )
            val comboB = comboRepository.save(
                Combo(
                    game = game,
                    memberId = memberB.id,
                    playerId = playerB.id,
                    now = comboCreatedDateTime,
                )
            )

            comboService.updateComboToFail(gameId = game.id)

            comboRepository.getById(comboA.id).comboStatus shouldBe ComboStatus.FAIL
            comboRepository.getById(comboB.id).comboStatus shouldBe ComboStatus.FAIL
        }

        expect("FAIL 상태로 처리할 때 진행 중인 게임이 아니면 예외가 발생한다.") {
            val game = gameRepository.save(
                getGame(
                    gameState = GameState.PENDING,
                    gameStartDateTime = gameStartDateTime
                ).sample()
            )

            shouldThrowWithMessage<IllegalArgumentException>("게임이 종료되지 않은 경우, 콤보 실패 처리를 할 수 없습니다.") {
                comboService.updateComboToFail(gameId = game.id)
            }
        }
    }

}) {
}

private fun getMember() =
    fixture.giveMeKotlinBuilder<Member>()
        .setExp(Member::id, 0L)
        .setExp(Member::socialProvider, SocialProvider.KAKAO)

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
