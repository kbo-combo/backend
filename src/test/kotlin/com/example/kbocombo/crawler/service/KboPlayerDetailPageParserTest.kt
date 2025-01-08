package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.dto.NewPlayerData
import com.example.kbocombo.crawler.infrastructure.KboPlayerDetailPageParser
import com.example.kbocombo.domain.player.vo.PlayerPosition.HITTER
import com.example.kbocombo.domain.player.vo.Team.KIWOOM
import com.example.kbocombo.domain.player.vo.WebId
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.junit.jupiter.api.Test

@DisplayNameGeneration(ReplaceUnderscores::class)
class KboPlayerDetailPageParserTest {

    @Test
    fun 테스트() {
        // given
        val element = NewPlayerData(WebId(54410), HITTER, KIWOOM)
        val parser = KboPlayerDetailPageParser()
        parser.getPlayerProfile(listOf( element))

        // when

        // then
    }
}
