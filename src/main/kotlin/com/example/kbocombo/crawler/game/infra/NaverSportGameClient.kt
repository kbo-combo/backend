package com.example.kbocombo.crawler.game.infra

import com.example.kbocombo.crawler.common.application.NaverSportClient
import com.example.kbocombo.crawler.common.infra.dto.NaverApiResponse
import com.example.kbocombo.crawler.game.application.GameClient
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameState.PENDING
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.vo.Team
import com.example.kbocombo.player.vo.WebId
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class NaverSportGameClient(
    private val naverSportClient: NaverSportClient,
    private val playerRepository: PlayerRepository,
    private val objectMapper: ObjectMapper
) : GameClient {

    override fun findGames(gameDate: LocalDate): List<Game> {
        val games = getGames(gameDate)
        return games
            .filterNot { it.isAllStartGame() }
            .map { toGameEntity(it) }
    }

    private fun getGames(gameDate: LocalDate): List<NaverGameResponse> {
        val gameJson = naverSportClient.getGameListByDate(
            fields = "basic,schedule,baseball",
            upperCategoryId = "kbaseball",
            categoryId = "kbo",
            fromDate = gameDate,
            toDate = gameDate,
            size = 100
        )
        return objectMapper.readValue(
            gameJson,
            object : TypeReference<NaverApiResponse<NaverGameListApiResponse>>() {}).result.games
    }

    // 선발 투수 정보가 있다면, preview API를 통해 id 조회
    private fun toGameEntity(game: NaverGameResponse): Game {
        val previewData = if (game.hasStarter()) findPreview(game.gameId) else null
        return Game(
            gameCode = game.gameId,
            homeTeam = Team.fromTeamCode(game.homeTeamCode),
            awayTeam = Team.fromTeamCode(game.awayTeamCode),
            homeStartingPitcherId = findPitcherId(previewData?.homeStarter),
            awayStartingPitcherId = findPitcherId(previewData?.awayStarter),
            startDate = game.gameDate,
            startTime = game.gameDateTime.toLocalTime(),
            gameType = GameType.getGameTypeByDate(game.gameDate),
            gameState = PENDING
        )
    }

    private fun findPreview(gameCode: String): PreviewData {
        val jsonResponse = naverSportClient.getGamePreview(gameCode)
        val apiResponseObject =
            objectMapper.readValue(jsonResponse, object : TypeReference<NaverApiResponse<PreviewResponse>>() {})
        return apiResponseObject.result.previewData
    }

    private fun findPitcherId(starterInfo: StarterInfo?) =
        starterInfo?.playerInfo?.pCode?.let { playerRepository.findByWebId(WebId(it)) }?.id
}

data class NaverGameListApiResponse(
    val games: List<NaverGameResponse>
)

data class NaverGameResponse(
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
    val homeTeamEmblemUrl: String?,
    val awayTeamEmblemUrl: String?,
    val widgetEnable: Boolean,
    val gameOnAir: Boolean,
    val specialMatchInfo: String?,
    val homeStarterName: String?,
    val awayStarterName: String?,
    val winPitcherName: String?,
    val losePitcherName: String?,
    val homeCurrentPitcherName: String?,
    val awayCurrentPitcherName: String?,
    val seriesGameNo: String?,
    val roundName: String?,
    val roundGameNo: String?,
// 포스트시즌 시리즈 데이터
//    val seriesOutcome: List<Series>?
) {

    fun hasStarter(): Boolean {
        return !homeStarterName.isNullOrBlank() && !awayStarterName.isNullOrBlank()
    }

    // 올스타 TeamCode 드림 / 나눔 형식이라 에러 발생가능
    fun isAllStartGame(): Boolean {
        return gameId.startsWith("9999")
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
