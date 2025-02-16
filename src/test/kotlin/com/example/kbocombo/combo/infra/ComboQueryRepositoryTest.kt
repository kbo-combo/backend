package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.domain.vo.SocialProvider
import com.example.kbocombo.member.infra.MemberRepository
import com.example.kbocombo.player.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ComboQueryRepositoryTest(
    private val comboQueryRepository: ComboQueryRepository,
    private val comboRepository: ComboRepository,
    private val playerRepository: PlayerRepository,
    private val memberRepository: MemberRepository,
    private val gameRepository: GameRepository,
) : ExpectSpec({

    context("콤보를 조회한다.") {
        expect("memberId와 gameDate로 콤보를 조회한다.") {
            val member = memberRepository.save(getMember().sample())
            val player = playerRepository.save(getPlayer().sample())
            val gameDate = LocalDateTime.of(2025, 3, 24, 18, 30)
            val game = gameRepository.save(getGame(startDateTime = gameDate).sample())
            comboRepository.save(
                Combo(
                    memberId = member.id,
                    game = game,
                    playerId = player.id,
                    now = gameDate.minusHours(1)
                )
            )

            val comboQueryDto = comboQueryRepository.findByComboParams(
                memberId = member.id,
                gameId = null,
                gameDate = gameDate.toLocalDate()
            )

            comboQueryDto shouldNotBe null
            comboQueryDto!!.combo.memberId shouldBe member.id
            comboQueryDto.player.id shouldBe player.id
        }

        expect("memberId와 gameId로 콤보를 조회한다.") {
            val member = memberRepository.save(getMember().sample())
            val player = playerRepository.save(getPlayer().sample())
            val gameDate = LocalDateTime.of(2025, 3, 24, 18, 30)
            val game = gameRepository.save(getGame(startDateTime = gameDate).sample())
            comboRepository.save(
                Combo(
                    memberId = member.id,
                    game = game,
                    playerId = player.id,
                    now = gameDate.minusHours(1)
                )
            )

            val comboQueryDto = comboQueryRepository.findByComboParams(
                memberId = member.id,
                gameId = game.id,
                gameDate = null
            )

            comboQueryDto shouldNotBe null
            comboQueryDto!!.combo.memberId shouldBe member.id
            comboQueryDto.player.id shouldBe player.id
        }
    }

})

private fun getPlayer() = fixture.giveMeKotlinBuilder<Player>()
    .setExp(Player::id, 0L)
    .setExp(Player::isRetired, false)

private fun getMember() = fixture.giveMeKotlinBuilder<Member>()
    .setExp(Member::id, 0L)
    .setExp(Member::socialProvider, SocialProvider.KAKAO)

private fun getGame(
    startDateTime: LocalDateTime
) = fixture.giveMeKotlinBuilder<Game>()
    .setExp(Game::id, 0L)
    .setExp(Game::startDate, startDateTime.toLocalDate())
    .setExp(Game::startTime, startDateTime.toLocalTime())
    .setExp(Game::gameState, GameState.PENDING)
