package com.example.kbocombo.crawler.infra

import com.example.kbocombo.crawler.application.NaverSportClient
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDate
import java.time.LocalDateTime

class NaverSportGameHandler(
    private val naverSportClient: NaverSportClient,
    private val objectMapper: ObjectMapper,
) {

    fun findGames(gameDate: LocalDate) {
        val gameJson = naverSportClient.getGameListByDate(
            upperCategoryId = "kbaseball",
            fromDate = gameDate,
            toDate = gameDate,
            size = 10
        )
//        val games = objectMapper.readValue(gameJson, GameList::class.java).games
//            .map {
//                Game(
//                    homeTeam = Team.fromTeamCode(it.homeTeamCode),
//                    awayTeam = Team.fromTeamCode(it.awayTeamCode),
//                    startDate = it.gameDate,
//                    startTime = it.gameDateTime.toLocalTime(),
//                    gameType = GameType.getGameTypeByDate(it.gameDate),
//                    gameState = GameState.PENDING
//                )
//            }
    }
}

data class GameList(
    val code: Int,
    val success: Boolean,
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
