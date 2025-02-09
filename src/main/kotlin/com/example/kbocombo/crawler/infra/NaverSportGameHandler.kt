package com.example.kbocombo.crawler.infra

import com.example.kbocombo.crawler.application.NaverSportClient
import com.example.kbocombo.crawler.infra.dto.NaverApiResponse
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class NaverSportGameHandler(
    private val naverSportClient: NaverSportClient,
    private val objectMapper: ObjectMapper
) {

    fun findGames(gameDate: LocalDate): NaverApiResponse<GameListResponse> {
        val gameJson = naverSportClient.getGameListByDate(
            upperCategoryId = "kbaseball",
            fromDate = gameDate,
            toDate = gameDate,
            size = 10
        )
        return objectMapper.readValue(gameJson, object : TypeReference<NaverApiResponse<GameListResponse>>() {})
    }

    fun findPreview(gameCode: String): NaverApiResponse<PreviewResponse> {
        val json = naverSportClient.getGamePreview(gameCode)
        return objectMapper.readValue(json, object : TypeReference<NaverApiResponse<PreviewResponse>>() {})
    }
}


data class GameListResponse(
    val games: List<NaverGame>
)

data class NaverGame(
    val gameId: String,
    val categoryId: String,
    val gameDate: LocalDate,
    val gameDateTime: LocalDateTime,
    val homeTeamCode: String,
    val homeTeamName: String,
    val homeTeamScore: Int,
    val awayTeamCode: String,
    val awayTeamName: String,
    val awayTeamScore: Int,
    val winner: String,
    val statusCode: String,
    val statusInfo: String,
    val cancel: Boolean,
    val suspended: Boolean,
    val reversedHomeAway: Boolean,
    val homeTeamEmblemUrl: String,
    val awayTeamEmblemUrl: String,
    val widgetEnable: Boolean
)

data class PreviewResponse(
    val previewData: PreviewData
)

data class PreviewData(
    val homeStarter: StarterInfo,
    val awayStarter: StarterInfo,
)

data class StarterInfo (
    val playerInfo: StarterPlayerInfo
)

data class StarterPlayerInfo(
    val backnum : String?,
    val hitType: String?,
    val pCode: String?,
    val name: String?,
    val birth: String?,
    val weight: String?,
    val height: String?,
)
