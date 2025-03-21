package com.example.kbocombo.crawler.player.infra

import com.example.kbocombo.common.logWarn
import com.example.kbocombo.crawler.common.utils.toHittingHand
import com.example.kbocombo.crawler.common.utils.toPitchingHand
import com.example.kbocombo.crawler.common.utils.toPlayerDetailPosition
import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.vo.HittingHandType
import com.example.kbocombo.player.vo.PitchingHandType
import com.example.kbocombo.player.vo.PlayerDetailPosition
import com.example.kbocombo.player.vo.PlayerDraftInfo
import com.example.kbocombo.player.vo.PlayerImage
import com.example.kbocombo.player.vo.WebId
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Component
class KboPlayerDetailPageParser {


    fun getPlayerProfile(playerData: WebPlayerInfo): Player? {
        return runCatching { toPlayer(playerData) }
            .onFailure { e -> logWarn("Failed to parse player with ${playerData.webId}", e) }
            .getOrNull()
    }


    /**
     * 팀은 전체 페이지와 상세 페이지에 다르게 명시될 수 있음
     * 상무로 save하는 경우 방지하기 위해 전체 페이지로 설정
     */
    private fun toPlayer(playerData: WebPlayerInfo): Player? {
        val document = getDocument(playerData.webId)
        return Player(
            name = getName(document),
            height = getHeight(document),
            weight = getWeight(document),
            hittingHandType = getHittingHandType(document),
            pitchingHandType = getPitchingHandType(document),
            playerDraftInfo = PlayerDraftInfo(getDraftDetail(document), getDraftYear(document)),
            position = playerData.position,
            detailPosition = getPlayerDetailPosition(document),
            birthDate = getBirthDate(document),
            team = playerData.team,
            playerImage = getPlayerImageUrl(document),
            webId = playerData.webId
        )
    }

    private fun getDocument(webId: WebId): Document =
        Jsoup.connect(getUrl(webId)).get()

    private fun getUrl(webId: WebId): String {
        return "https://www.koreabaseball.com/Record/Player/HitterDetail/Basic.aspx?playerId=${webId.value}"
    }

    private fun getName(document: Document): String {
        return document.getTextById(NAME_ID)
    }

    private fun getDraftDetail(document: Document): String {
        return document.getTextById(DRAFT_INFO_ID)
    }

    private fun getDraftYear(document: Document): Int {
        val text = document.getTextById(DRAFT_INFO_ID)
        val rawYear = text.substringBefore(" ").toInt()
        return if (rawYear < 2000) rawYear + 1900 else rawYear + 2000
    }

    private fun getWeight(document: Document): Int {
        val heightWithWeight = document.getTextById(HEIGHT_WITH_WEIGHT_ID)
        return heightWithWeight.substringAfter("/").substringBefore("kg").toInt()
    }

    private fun getHeight(document: Document): Int {
        val heightWithWeight = document.getTextById(HEIGHT_WITH_WEIGHT_ID)
        return heightWithWeight.substringAfter(" ").substringBefore("cm").toInt()
    }

    private fun getPitchingHandType(document: Document): PitchingHandType {
        val rawPosition = document.getTextById(POSITION_ID)
        return toPitchingHand(rawPosition.substringAfter("(").take(2))
    }

    private fun getHittingHandType(document: Document): HittingHandType {
        val rawPosition = document.getTextById(POSITION_ID)
        return toHittingHand(rawPosition.substringBefore(")").takeLast(2))
    }

    private fun getBirthDate(document: Document): LocalDate {
        val birthDate = document.getTextById(BIRTH_DAY_ID)
        return LocalDate.parse(birthDate, formatter)
    }

    private fun getPlayerDetailPosition(document: Document): PlayerDetailPosition {
        val rawPosition = document.getTextById(POSITION_ID).substringBefore("(")
        return toPlayerDetailPosition(rawPosition)
    }

    private fun getPlayerImageUrl(document: Document): PlayerImage {
        val imageUrl = document.select(IMAGE_KEY)
            .attr("src")
            .substringAfter("//")
            .takeIf { it.isNotBlank() }
        return PlayerImage(imageUrl)
    }


    fun Document.getTextById(id: String): String {
        val element = this.getElementById(id)
        return element?.text()
            ?: throw IllegalArgumentException("Element with ID '$id' not found in the document.")
    }

    private companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN)
        private const val PREFIX = "cphContents_cphContents_cphContents_playerProfile_"
        private const val NAME_ID = "${PREFIX}lblName"
        private const val IMAGE_KEY = "img#${PREFIX}imgProgile"
        private const val POSITION_ID = "${PREFIX}lblPosition"
        private const val HEIGHT_WITH_WEIGHT_ID = "${PREFIX}lblHeightWeight"
        private const val DRAFT_INFO_ID = "${PREFIX}lblDraft"
        private const val BIRTH_DAY_ID = "${PREFIX}lblBirthday"
    }
}
