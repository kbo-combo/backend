package com.example.kbocombo.crawler.infrastructure

import com.example.kbocombo.crawler.dto.NewPlayerData
import com.example.kbocombo.crawler.utils.toHittingHand
import com.example.kbocombo.crawler.utils.toPitchingHand
import com.example.kbocombo.crawler.utils.toPlayerDetailPosition
import com.example.kbocombo.domain.player.Player
import com.example.kbocombo.domain.player.vo.HittingHandType
import com.example.kbocombo.domain.player.vo.PitchingHandType
import com.example.kbocombo.domain.player.vo.PlayerDetailPosition
import com.example.kbocombo.domain.player.vo.WebId
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Component
class KboPlayerDetailPageParser {

    private val logger = LoggerFactory.getLogger(KboPlayerDetailPageParser::class.java)

    fun getPlayerProfile(playerData: List<NewPlayerData>): List<Player> {
        return playerData.mapNotNull {
            runCatching { toPlayer(it) }
                .onFailure { e ->
                    logger.error("Failed to parse player with webId=${it.webId.value}", e)
                }
                .getOrNull()
        }
    }

    private fun toPlayer(playerData: NewPlayerData): Player? {
        val document = getDocument(playerData.webId)
        return Player(
            name = getName(document),
            height = getHeight(document),
            weight = getWeight(document),
            hittingHandType = getHittingHandType(document),
            pitchingHandType = getPitchingHandType(document),
            draftInfo = getDraftInfo(document),
            draftYear = getDraftYear(document),
            position = playerData.position,
            detailPosition = getPlayerDetailPosition(document),
            birthDate = getBirthDate(document),
            team = playerData.team,
            imageUrl = getPlayerImageUrl(document),
            webId = playerData.webId
        )
    }

    private fun getDocument(webId: WebId): Document =
        Jsoup.connect(getUrl(webId)).get()

    private fun getUrl(webId: WebId): String {
        return "https://www.koreabaseball.com/Record/Player/HitterDetail/Basic.aspx?playerId=${webId.value}"
    }

    private fun getName(document: Document): String {
        return document.getElementById(NAME_ID).text()
    }

    private fun getDraftInfo(document: Document): String {
        return document.getElementById(DRAFT_INFO_ID).text()
    }

    private fun getDraftYear(document: Document): Int {
        val text = document.getElementById(DRAFT_INFO_ID)?.text().orEmpty()
        val rawYear = text.substringBefore(" ").toInt()
        return if (rawYear < 2000) rawYear + 1900 else rawYear + 2000
    }

    private fun getWeight(document: Document): Int {
        val heightWithWeight = document.getElementById(HEIGHT_WITH_WEIGHT_ID).text()
        return heightWithWeight.substringAfter("/").substringBefore("kg").toInt()
    }

    private fun getHeight(document: Document): Int {
        val heightWithWeight = document.getElementById(HEIGHT_WITH_WEIGHT_ID).text()
        return heightWithWeight.substringAfter(" ").substringBefore("cm").toInt()
    }

    private fun getPitchingHandType(document: Document): PitchingHandType {
        val rawPosition = document.getElementById(POSITION_ID).text()
        return toPitchingHand(rawPosition.substringAfter("(").take(2))
    }

    private fun getHittingHandType(document: Document): HittingHandType {
        val rawPosition = document.getElementById(POSITION_ID).text()
        return toHittingHand(rawPosition.substringBefore(")").takeLast(2))
    }

    private fun getBirthDate(document: Document): LocalDate {
        val birthDate = document.getElementById(BIRTH_DAY_ID).text()
        return LocalDate.parse(birthDate, formatter)
    }

    private fun getPlayerDetailPosition(document: Document): PlayerDetailPosition {
        val rawPosition = document.getElementById(POSITION_ID).text().substringBefore("(")
        return toPlayerDetailPosition(rawPosition)
    }

    private fun getPlayerImageUrl(document: Document): String {
        return document.select(IMAGE_KEY)
            .attr("src")
            .substringAfter("//")
    }

    private companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN)
        private const val PREFIX = "cphContents_cphContents_cphContents_playerProfile_"
        private const val NAME_ID = "${PREFIX}lblName"
        private const val IMAGE_KEY = "img#${PREFIX}\"imgProgile"
        private const val POSITION_ID = "${PREFIX}lblPosition"
        private const val HEIGHT_WITH_WEIGHT_ID = "${PREFIX}lblHeightWeight"
        private const val DRAFT_INFO_ID = "${PREFIX}lblDraft"
        private const val BIRTH_DAY_ID = "${PREFIX}lblBirthday"
    }
}
