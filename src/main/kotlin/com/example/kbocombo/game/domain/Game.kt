package com.example.kbocombo.game.domain

import com.example.kbocombo.common.BaseEntity
import com.example.kbocombo.game.domain.vo.GameState
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.player.vo.Team
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(name = "GAME")
class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "game_code", nullable = false)
    val gameCode: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "home_team", nullable = false)
    val homeTeam: Team,

    homeStartingPitcherId: Long?,

    awayStartingPitcherId: Long?,

    gameState: GameState = GameState.PENDING,

    @Enumerated(EnumType.STRING)
    @Column(name = "away_team", nullable = false)
    val awayTeam: Team,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate,

    startTime: LocalTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    val gameType: GameType,

    ) : BaseEntity() {

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state", nullable = false)
    var gameState: GameState = gameState
        protected set

    @Column(name = "home_starting_pitcher_id")
    var homeStartingPitcherId: Long? = homeStartingPitcherId
        protected set

    @Column(name = "away_starting_pitcher_id")
    var awayStartingPitcherId: Long? = awayStartingPitcherId
        protected set

    @Column(name = "start_time", nullable = false)
    var startTime: LocalTime = startTime
        protected set

    fun updateGameData(gameStartTime: LocalTime, homeStartingPitcherId: Long?, awayStartingPitcherId: Long?) {
        this.homeStartingPitcherId = homeStartingPitcherId
        this.awayStartingPitcherId = awayStartingPitcherId
        this.startTime = gameStartTime
    }

    fun isRunning(): Boolean {
        return gameState == GameState.RUNNING
    }

    fun isCompleted(): Boolean {
        return gameState == GameState.COMPLETED
    }

    fun isCancelled(): Boolean {
        return gameState == GameState.CANCEL
    }

    fun isAfterGameStart(dateTime: LocalDateTime): Boolean {
        return LocalDateTime.of(startDate, startTime) >= dateTime
    }

    fun cancel() {
        this.gameState = GameState.CANCEL
    }

    fun complete() {
        this.gameState = GameState.COMPLETED
    }

    fun start() {
        this.gameState = GameState.RUNNING
    }
}
