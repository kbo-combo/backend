package com.example.kbocombo.crawler.game.infra

import com.example.kbocombo.crawler.common.application.NaverSportClient
import com.example.kbocombo.crawler.common.infra.dto.NaverApiResponse
import com.example.kbocombo.crawler.game.application.GameClient
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameState.PENDING
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.player.vo.Team
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class NaverSportGameClient(
    private val naverSportClient: NaverSportClient,
    private val objectMapper: ObjectMapper
) : GameClient {

    override fun findGames(gameDate: LocalDate): List<Game> {
        val games = getGames(gameDate)
        return games.map(::toGameEntity)
    }

    private fun getGames(gameDate: LocalDate): List<GameResponse> {
        val gameJson = naverSportClient.getGameListByDate(
            upperCategoryId = "kbaseball",
            fromDate = gameDate,
            toDate = gameDate,
            size = 100
        )
        return objectMapper.readValue(
            gameJson,
            object : TypeReference<NaverApiResponse<GameListApiResponse>>() {}).result.games
    }

    /**
     * 선발투수 데이터가 있으면 preview API를 통해 선발 투수 데이터를 조회.
     */
    private fun toGameEntity(game: GameResponse): Game {
        val previewData = if (game.hasStarter()) findPreview(game.gameId) else null
        return Game(
            gameCode = game.gameId,
            homeTeam = Team.fromTeamCode(game.homeTeamCode),
            awayTeam = Team.fromTeamCode(game.awayTeamCode),
            homeStartingPitcherId = previewData?.homeStarter?.playerInfo?.pCode?.toLongOrNull(),
            awayStartingPitcherId = previewData?.awayStarter?.playerInfo?.pCode?.toLongOrNull(),
            startDate = game.gameDate,
            startTime = game.gameDateTime.toLocalTime(),
            gameType = GameType.getGameTypeByDate(game.gameDate),
            gameState = PENDING
        )
    }

    private fun findPreview(gameCode: String): PreviewData {
        val jsonResponse = naverSportClient.getGamePreview(gameCode)
        val apiResponseObject = objectMapper.readValue(jsonResponse, object : TypeReference<NaverApiResponse<PreviewResponse>>() {})
        return apiResponseObject.result.previewData
    }
}

data class GameListApiResponse(
    val games: List<GameResponse>
)

data class GameResponse(
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
    val widgetEnable: Boolean,
    val gameOnAir: Boolean,
    val specialMatchInfo : String?,
    val seriesOutcome: String?,
    val homeStarterName: String?,
    val awayStarterName: String?,
    val winPitcherName: String?,
    val losePitcherName: String?,
    val homeCurrentPitcherName: String?,
    val awayCurrentPitcherName: String?,
    val seriesGameNo: String?,
    val roundName: String?,
    val roundGameNo: String?
) {

    fun hasStarter(): Boolean {
        return !homeStarterName.isNullOrBlank() && !awayStarterName.isNullOrBlank()
    }
}

data class PreviewResponse(
    val previewData: PreviewData
)

data class PreviewData(
    val homeStarter: StarterInfo,
    val awayStarter: StarterInfo,
)

data class StarterInfo(
    val playerInfo: StarterPlayerInfo
)

data class StarterPlayerInfo(
    val backnum: String?,
    val hitType: String?,
    val pCode: String?,
    val name: String?,
    val birth: String?,
    val weight: String?,
    val height: String?,
)
