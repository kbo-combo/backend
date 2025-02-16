package com.example.kbocombo.game.application

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.infra.GameQueryRepository
import com.example.kbocombo.player.Player
import com.example.kbocombo.player.vo.Team
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.Year

@Service
class GameQueryService(
    private val gameQueryRepository: GameQueryRepository
) {

    fun findByGameDate(gameDate: LocalDate): List<GameResponse> {
        val games = gameQueryRepository.findAllGameByDate(gameDate)
        val playerIds = games
            .flatMap { listOfNotNull(it.homeStartingPitcherId, it.awayStartingPitcherId) }
        val playersById = gameQueryRepository.findAllPlayerIdIn(playerIds)
            .associateBy { it.id }
        return GameResponse.toList(games, playersById)
    }

    fun findAllByYearAndMonth(year: Year, month: Month): List<GameYearMonthResponse> {
        val start = year.atMonth(month).atDay(1)
        val end = year.atMonth(month).atEndOfMonth()
        val gameDates = gameQueryRepository.findAllGameByBetweenDate(start = start, end = end)
        return GameYearMonthResponse.toList(gameDates)
    }
}

data class GameResponse(
    val id: Long,
    val homeTeam: Team,
    val awayTeam: Team,
    val startDate: LocalDate,
    val startTime: LocalTime,
    val gameState: GameState,
    val homeStartingPitcher: StartingPitcherResponse?,
    val awayStartingPitcher: StartingPitcherResponse?
) {

    companion object {

        fun toList(games: List<Game>, playersById: Map<Long, Player>): List<GameResponse> {
            return games.map {
                GameResponse(
                    id = it.id,
                    homeTeam = it.homeTeam,
                    awayTeam = it.awayTeam,
                    startDate = it.startDate,
                    startTime = it.startTime,
                    gameState = it.gameState,
                    homeStartingPitcher = StartingPitcherResponse.from(playersById[it.homeStartingPitcherId]),
                    awayStartingPitcher = StartingPitcherResponse.from(playersById[it.awayStartingPitcherId]),
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
