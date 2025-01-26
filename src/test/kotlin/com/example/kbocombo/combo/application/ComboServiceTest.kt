package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.infra.FakeGameRepository
import com.example.kbocombo.combo.ui.request.ComboCreateRequest
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.mock.infra.FakeComboRepository
import com.example.kbocombo.player.Player
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import java.time.LocalDateTime

@DisplayNameGeneration(ReplaceUnderscores::class)
class ComboServiceTest : StringSpec({

    lateinit var sut: ComboService
    lateinit var gameRepository: FakeGameRepository
    lateinit var comboRepository: FakeComboRepository

    beforeTest {
        gameRepository = FakeGameRepository()
        comboRepository = FakeComboRepository()
        sut = ComboService(gameRepository, comboRepository)
    }

    "게임 시작 10분전 이면 콤보 생성이 불가능하다" {
        val game = getGame()
            .setExp(Game::startDateTime, LocalDateTime.parse("2025-01-25T18:30:00"))
            .sample()
        val player = getPlayer().sample()
        gameRepository.save(game)
        val member = getMember()
        val now = LocalDateTime.parse("2025-01-25T18:20:01")

        val actual = shouldThrow<IllegalArgumentException> {
            sut.createCombo(member, ComboCreateRequest(gameId = game.id, playerId = player.id), now)
        }

        actual.message shouldContain "게임 시작 10분 전"
    }

    "콬보를 정상적으로 생성한다" {
        val game = getGame()
            .setExp(Game::startDateTime, LocalDateTime.parse("2025-01-25T18:30:00"))
            .sample()
        val player = getPlayer().sample()
        gameRepository.save(game)
        val member = getMember()
        val now = LocalDateTime.parse("2025-01-25T12:00:00")

        sut.createCombo(member, ComboCreateRequest(gameId = game.id, playerId = player.id), now)

        comboRepository.findAll() shouldHaveSize 1
    }
})


private fun getGame() = fixture.giveMeKotlinBuilder<Game>()
    .setExp(Game::id, 0L)

private fun getPlayer() = fixture.giveMeKotlinBuilder<Player>()
    .setExp(Player::id, 0L)

private fun getMember() = fixture.giveMeKotlinBuilder<Member>().sample()
