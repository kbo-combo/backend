package com.example.kbocombo.crawler.player.infra

import com.example.kbocombo.player.vo.PlayerDetailPosition
import com.example.kbocombo.player.vo.PlayerPosition.HITTER
import com.example.kbocombo.player.vo.Team.NC
import com.example.kbocombo.player.vo.WebId
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class KboPlayerDetailPageParserTest : StringSpec({

    lateinit var sut: KboPlayerDetailPageParser

    beforeTest {
        sut = KboPlayerDetailPageParser()
    }

    "플레이어 프로필을 성공적으로 조회한다" {
        // given
        val webPlayerInfo = WebPlayerInfo(WebId(51907), HITTER, NC)

        // when
        val actual = sut.getPlayerProfile(webPlayerInfo)!!

        // then
        actual.webId shouldBe webPlayerInfo.webId
        actual.name shouldBe "김주원"
        actual.detailPosition shouldBe PlayerDetailPosition.IN_FIELDER
    }
})
