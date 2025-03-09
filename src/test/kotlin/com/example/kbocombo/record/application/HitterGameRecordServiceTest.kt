package com.example.kbocombo.record.application

import com.example.kbocombo.crawler.game.infra.dto.HitterRecordDto
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.mock.config.TestConfig
import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.record.domain.HitterGameRecord
import com.example.kbocombo.record.domain.HitterHitRecordedEvent
import com.example.kbocombo.record.infra.HitterGameRecordRepository
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.transaction.annotation.Transactional

@Import(TestConfig::class)
@Transactional
@SpringBootTest
class HitterGameRecordServiceTest(
    private val sut: HitterGameRecordService,
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val hitterGameRecordRepository: HitterGameRecordRepository,
    private val applicationEvents: ApplicationEvents
) : ExpectSpec({

    beforeTest {
        applicationEvents.clear()
    }

    context("타자 타격 기록 저장") {

        expect("게임이 진행 예정이면 저장하지 않는다.") {
            val game = gameRepository.save(getGame(GameState.PENDING).sample())
            val playerA = playerRepository.save(getPlayer().sample())
            val records = listOf(HitterRecordDto(webId = playerA.webId, atBats = 0, hits = 0))

            sut.saveOrUpdateHitterRecords(gameId = game.id, hitterRecordDtos = records)

            hitterGameRecordRepository.findAllByGameId(gameId = game.id) shouldHaveSize 0
        }

        expect("게임이 진행 예정이 아니고 DB에 존재하지 않으면 저장한다.") {
            val game = gameRepository.save(getGame(GameState.RUNNING).sample())
            val playerA = playerRepository.save(getPlayer().sample())
            val playerB = playerRepository.save(getPlayer().sample())
            val records = listOf(
                HitterRecordDto(webId = playerA.webId, atBats = 0, hits = 0),
                HitterRecordDto(webId = playerB.webId, atBats = 0, hits = 0)
            )

            sut.saveOrUpdateHitterRecords(gameId = game.id, hitterRecordDtos = records)

            hitterGameRecordRepository.findAllByGameId(gameId = game.id) shouldHaveSize 2
        }

        expect("게임이 진행 예정이 아니고 DB에 존재하면 업데이트 한다.") {
            val game = gameRepository.save(getGame(GameState.RUNNING).sample())
            val player = playerRepository.save(getPlayer().sample())
            hitterGameRecordRepository.save(getHitterGameRecord(game, player).sample())
            val records = listOf(
                HitterRecordDto(webId = player.webId, atBats = 4, hits = 2),
            )

            sut.saveOrUpdateHitterRecords(gameId = game.id, hitterRecordDtos = records)

            val actual = hitterGameRecordRepository.findAllByGameId(gameId = game.id)[0]
            actual.publishHitterHitRecordEvent()
            actual.atBats shouldBe 4
            actual.hits shouldBe 2
        }

        expect("안타가 1이상으로 저장되면 이벤트를 발행한다.") {
            val game = gameRepository.save(getGame(GameState.RUNNING).sample())
            val player = playerRepository.save(getPlayer().sample())
            val records = listOf(
                HitterRecordDto(webId = player.webId, atBats = 1, hits = 1),
            )

            sut.saveOrUpdateHitterRecords(gameId = game.id, hitterRecordDtos = records)

            val event = applicationEvents.stream(HitterHitRecordedEvent::class.java).findFirst().get()!!
            event.playerId shouldBe player.id
            event.gameId shouldBe game.id
        }

        expect("안타가 0에서 1이상으로 변경되면 이벤트를 발행한다.") {
            val game = gameRepository.save(getGame(GameState.RUNNING).sample())
            val player = playerRepository.save(getPlayer().sample())
            hitterGameRecordRepository.save(getHitterGameRecord(game, player).sample())
            val records = listOf(
                HitterRecordDto(webId = player.webId, atBats = 1, hits = 1),
            )

            sut.saveOrUpdateHitterRecords(gameId = game.id, hitterRecordDtos = records)

            val event = applicationEvents.stream(HitterHitRecordedEvent::class.java).findFirst().get()!!
            event.playerId shouldBe player.id
            event.gameId shouldBe game.id
        }
    }
})

private fun getGame(gameState: GameState) = fixture.giveMeKotlinBuilder<Game>()
    .setExp(Game::id, 0L)
    .setExp(Game::gameType, GameType.REGULAR_SEASON)
    .setExp(Game::gameState, gameState)

private fun getPlayer() = fixture.giveMeKotlinBuilder<Player>()
    .setExp(Player::id, 0L)
    .setExp(Player::isRetired, false)

private fun getHitterGameRecord(
    game: Game,
    player: Player
)= fixture.giveMeKotlinBuilder<HitterGameRecord>()
    .setExp(HitterGameRecord::id, 0L)
    .setExp(HitterGameRecord::gameId, game.id)
    .setExp(HitterGameRecord::gameDate, game.startDate)
    .setExp(HitterGameRecord::playerId, player.id)
    .setExp(HitterGameRecord::hits, 0)
    .setExp(HitterGameRecord::atBats, 0)
