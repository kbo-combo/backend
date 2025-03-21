package com.example.kbocombo.crawler.game.infra

import com.example.kbocombo.crawler.common.application.NaverSportClient
import com.example.kbocombo.crawler.common.infra.dto.NaverApiResponse
import com.example.kbocombo.crawler.game.application.GameClient
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameScore
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.vo.Team
import com.example.kbocombo.player.vo.WebId
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class NaverSportGameClient(
    private val naverSportClient: NaverSportClient,
    private val playerRepository: PlayerRepository,
    private val objectMapper: ObjectMapper
) : GameClient {

    override fun findGames(gameDate: LocalDate): List<GameDto> {
        val games = getGames(gameDate)
        return games
            .filterNot { it.isAllStartGame() }
            .map { toGameDto(it) }
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
    private fun toGameDto(naverGameResponse: NaverGameResponse): GameDto {
        val previewData = if (naverGameResponse.hasStartingPitcher()) findPreview(naverGameResponse.gameId) else null
        val statusCode = naverGameResponse.statusCode
        val statusInfo = naverGameResponse.statusInfo

        return GameDto(
            gameCode = naverGameResponse.gameId,
            homeTeam = Team.fromTeamCode(naverGameResponse.homeTeamCode),
            awayTeam = Team.fromTeamCode(naverGameResponse.awayTeamCode),
            homeStartingPitcherId = findPitcherId(previewData?.homeStarter),
            awayStartingPitcherId = findPitcherId(previewData?.awayStarter),
            startDate = naverGameResponse.gameDate,
            startTime = naverGameResponse.gameDateTime.toLocalTime(),
            gameType = GameType.getGameTypeByDate(naverGameResponse.gameDate),
            gameState = convertToGameStatus(statusCode = statusCode, statusInfo = statusInfo),
            gameScore = GameScore(homeTeamScore = naverGameResponse.homeTeamScore, awayTeamScore = naverGameResponse.awayTeamScore),
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

    private fun convertToGameStatus(statusCode: String, statusInfo: String): GameState? {
        return when (statusCode) {
            "BEFORE" -> if (statusInfo == "경기취소") GameState.CANCEL else GameState.PENDING
            "STARTED" -> GameState.RUNNING
            "RESULT" -> GameState.COMPLETED
            else -> null
        }
    }
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

    fun hasStartingPitcher(): Boolean {
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

data class GameDto(
    val gameCode: String,
    val homeTeam: Team,
    val awayTeam: Team,
    val homeStartingPitcherId: Long?,
    val awayStartingPitcherId: Long?,
    val startDate: LocalDate,
    val startTime: LocalTime,
    val gameType: GameType,
    val gameState: GameState? = null,
    val gameScore: GameScore,
)

fun GameDto.toEntity(): Game {
    return Game(
        gameCode = gameCode,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        homeStartingPitcherId = homeStartingPitcherId,
        awayStartingPitcherId = awayStartingPitcherId,
        gameType = gameType,
        gameState = gameState ?: GameState.PENDING,
        startDate = startDate,
        startTime = startTime,
    )
}
