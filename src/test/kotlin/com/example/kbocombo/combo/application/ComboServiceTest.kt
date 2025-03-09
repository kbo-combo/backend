package com.example.kbocombo.combo.application

import com.example.kbocombo.annotation.IntegrationTest
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
import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@IntegrationTest
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

        expect("SUCCESS 상태로 처리할 때 진행 중인 게임이 아니면 return") {
            val member = memberRepository.save(getMember().sample())
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
                    memberId = member.id,
                    playerId = player.id,
                    now = comboCreatedDateTime,
                )
            )

            comboService.updateComboToSuccess(gameId = game.id, playerWebId = player.webId.value)

            comboRepository.getById(combo.id).comboStatus shouldBe ComboStatus.PENDING
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

        expect("PASS가 아닌 모든 게임을 PASS로 변경한다") {
            val memberA = memberRepository.save(getMember().sample())
            val memberB = memberRepository.save(getMember().sample())
            val memberC = memberRepository.save(getMember().sample())
            val game = gameRepository.save(
                getGame(
                    gameState = GameState.CANCEL,
                    gameStartDateTime = gameStartDateTime
                ).sample()
            )
            val comboA = comboRepository.save(
                getCombo(
                    game = game,
                    member = memberA,
                    comboStatus = ComboStatus.PENDING
                ).sample()
            )
            val comboB = comboRepository.save(
                getCombo(
                    game = game,
                    member = memberB,
                    comboStatus = ComboStatus.SUCCESS
                ).sample()
            )
            val comboC =
                comboRepository.save(getCombo(game = game, member = memberC, comboStatus = ComboStatus.PASS).sample())

            comboService.updateComboToPass(gameId = game.id, now = gameStartDateTime.plusMinutes(1))

            comboRepository.getById(comboA.id).comboStatus shouldBe ComboStatus.PASS
            comboRepository.getById(comboB.id).comboStatus shouldBe ComboStatus.PASS
            comboRepository.getById(comboC.id).comboStatus shouldBe ComboStatus.PASS
        }

        expect("취소 상태인 게임일때만 PASS 처리가 가능하다") {
            val givenGameStates = GameState.entries.filterNot { it == GameState.CANCEL }
            givenGameStates.forAll { gameState ->
                val game =
                    gameRepository.save(getGame(gameState = gameState, gameStartDateTime = gameStartDateTime).sample())

                shouldThrowWithMessage<IllegalArgumentException>("게임이 취소되지 않은 경우, 콤보 패스 처리를 할 수 없습니다.") {
                    comboService.updateComboToPass(gameId = game.id, now = gameStartDateTime.plusMinutes(1))
                }
            }
        }
    }
})

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

private fun getCombo(
    game: Game,
    member: Member,
    comboStatus: ComboStatus,
) = fixture.giveMeKotlinBuilder<Combo>()
    .setExp(Combo::id, 0L)
    .setExp(Combo::memberId, member.id)
    .setExp(Combo::game, game)
    .setExp(Combo::comboStatus, comboStatus)
    .setExp(Combo::gameDate, game.startDate)
