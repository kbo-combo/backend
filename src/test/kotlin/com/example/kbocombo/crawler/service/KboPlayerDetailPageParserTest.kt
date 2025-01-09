package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.dto.NewPlayerData
import com.example.kbocombo.crawler.infrastructure.KboPlayerDetailPageParser
import com.example.kbocombo.domain.player.vo.PlayerPosition.HITTER
import com.example.kbocombo.domain.player.vo.Team.NC
import com.example.kbocombo.domain.player.vo.WebId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.junit.jupiter.api.Test

@DisplayNameGeneration(ReplaceUnderscores::class)
class KboPlayerDetailPageParserTest {

    private lateinit var kboPlayerDetailPageParser: KboPlayerDetailPageParser

    @BeforeEach
    fun setUp() {
        kboPlayerDetailPageParser = KboPlayerDetailPageParser()
    }

    @Test
    fun 테스트() {
        // given

        // when
        val actual = kboPlayerDetailPageParser.getPlayerProfile(NewPlayerData(WebId(51907), HITTER, NC))

        // then
    }
}
