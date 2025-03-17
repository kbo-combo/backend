package com.example.kbocombo.game.application

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameScore
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.infra.GameQueryRepository
import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.vo.Team
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.Year

@Service
class GameQueryService(
    private val gameQueryRepository: GameQueryRepository
) {

    @Transactional(readOnly = true)
    fun findAllGamesByDate(gameDate: LocalDate): List<GameByDateResponse> {
        val games = gameQueryRepository.findAllGamesByDate(gameDate)
        val playerIds = games
            .flatMap { listOfNotNull(it.homeStartingPitcherId, it.awayStartingPitcherId) }
        val playersById = gameQueryRepository.findAllPlayerIdIn(playerIds)
            .associateBy { it.id }
        return GameByDateResponse.toList(games, playersById)
    }

    @Transactional(readOnly = true)
    fun findAllByYearAndMonth(year: Year, month: Month): List<GameYearMonthResponse> {
        val start = year.atMonth(month).atDay(1)
        val end = year.atMonth(month).atEndOfMonth()
        val gameDates = gameQueryRepository.findAllGameByBetweenDate(start = start, end = end)
        return GameYearMonthResponse.toList(gameDates)
    }
}

data class GameByDateResponse(
    val id: Long,
    val homeTeam: Team,
    val awayTeam: Team,
    val startDate: LocalDate,
    val startTime: LocalTime,
    val gameState: GameState,
    val homeStartingPitcher: StartingPitcherResponse?,
    val awayStartingPitcher: StartingPitcherResponse?,
    val gameScore: GameScoreResponse?
) {

    companion object {

        fun toList(games: List<Game>, playersById: Map<Long, Player>): List<GameByDateResponse> {
            return games.map {
                GameByDateResponse(
                    id = it.id,
                    homeTeam = it.homeTeam,
                    awayTeam = it.awayTeam,
                    startDate = it.startDate,
                    startTime = it.startTime,
                    gameState = it.gameState,
                    homeStartingPitcher = StartingPitcherResponse.from(playersById[it.homeStartingPitcherId]),
                    awayStartingPitcher = StartingPitcherResponse.from(playersById[it.awayStartingPitcherId]),
                    gameScore = GameScoreResponse.from(it.gameScore)
                )
            }
        }
    }
}

data class StartingPitcherResponse(
    val pitcherId: Long,
    val name: String
) {

    companion object {
        fun from(player: Player?): StartingPitcherResponse? {
            if (player == null) {
                return null
            }
            return StartingPitcherResponse(pitcherId = player.id, name = player.name)
        }
    }
}

data class GameYearMonthResponse(
    val gameDate: LocalDate
) {
    companion object {
        fun toList(dates: List<LocalDate>): List<GameYearMonthResponse> {
            return dates.map {
                GameYearMonthResponse(gameDate = it)
            }
        }
    }
}


data class GameScoreResponse(
    val homeTeamScore: Int,
    val awayTeamScore: Int
) {
    companion object {
        fun from(gameScore: GameScore?): GameScoreResponse? {
            return gameScore?.let { GameScoreResponse(homeTeamScore = it.homeTeamScore, awayTeamScore =  it.awayTeamScore) }
        }
    }
}
