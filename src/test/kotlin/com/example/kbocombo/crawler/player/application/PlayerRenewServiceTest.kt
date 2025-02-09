package com.example.kbocombo.crawler.player.application

import com.example.kbocombo.mock.infra.FakePlayerRepository
import com.example.kbocombo.player.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.vo.PlayerImage
import com.example.kbocombo.utils.fixture
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores

@DisplayNameGeneration(ReplaceUnderscores::class)
class PlayerRenewServiceTest : StringSpec({

    lateinit var sut: PlayerRenewService
    lateinit var playerRepository: PlayerRepository

    beforeTest {
        playerRepository = FakePlayerRepository()
        sut = PlayerRenewService(playerRepository)
    }

    "DB에 WebID가 없으면 저장한다" {
        val player = getPlayer().sample()

        sut.renew(listOf(player))

        player.id shouldNotBe  0L
        playerRepository.findAllByIsRetiredFalse() shouldHaveSize 1
    }

    "DB에 WebID가 있지만 이미지가 다르면 업데이트한다" {
        val savedPlayer = playerRepository.save(getPlayer().sample())
        val recentPlayer = getPlayer()
            .setExp(Player::webId, savedPlayer.webId)
            .setExp(Player::playerImage, PlayerImage("절대겹칠수가없음이건"))
            .sample()

        sut.renew(listOf(recentPlayer))

        savedPlayer.playerImage.imageUrl shouldBe recentPlayer.playerImage.imageUrl
    }
})

private fun getPlayer() = fixture.giveMeKotlinBuilder<Player>()
    .setExp(Player::id, 0L)
    .setExp(Player::isRetired, false)
