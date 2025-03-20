package com.example.kbocombo.combo.domain

import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.common.BaseEntity
import com.example.kbocombo.exception.requireOrThrow
import com.example.kbocombo.game.domain.Game
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(name = "COMBO")
class Combo private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "member_id", nullable = false)
    val memberId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    var game: Game,

    @Column(name = "player_id", nullable = false)
    var playerId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "combo_status", nullable = false)
    var comboStatus: ComboStatus = ComboStatus.PENDING,

    @Column(name = "game_date", nullable = false)
    var gameDate: LocalDate,
) : BaseEntity() {

    constructor(
        memberId: Long,
        game: Game,
        playerId: Long,
        now: LocalDateTime
    ) : this(
        id = 0L,
        memberId = memberId,
        game = game,
        playerId = playerId,
        gameDate = game.startDate
    ) {
        checkCreate(game, now)
    }

    fun checkDelete(now: LocalDateTime) {
        requireOrThrow(isAllowedTimeBefore(game, now)) { "게임 시작 ${ALLOWED_MINUTES_GAP}분 이전에만 삭제할 수 있습니다." }
    }

    fun update(game: Game, playerId: Long, now: LocalDateTime) {
        requireOrThrow(isAllowedTimeBefore(game, now)) { "기존에 등록한 콤보가 게임 시작 ${ALLOWED_MINUTES_GAP}분 전 이라 삭제할 수 없습니다." }
        checkCreate(game, now)
        this.game = game
        this.playerId = playerId
        this.gameDate = game.startDate
    }

    fun success() {
        this.comboStatus = ComboStatus.SUCCESS
    }

    fun fail() {
        this.comboStatus = ComboStatus.FAIL
    }

    fun pass() {
        this.comboStatus = ComboStatus.PASS
    }

    fun isPassed(): Boolean {
        return this.comboStatus == ComboStatus.PASS
    }

    private fun checkCreate(game: Game, now: LocalDateTime) {
        requireOrThrow(isAllowedTimeBefore(game, now)) {
            "게임 시작 ${ALLOWED_MINUTES_GAP}분 이전에만 등록할 수 있습니다."
        }

        requireOrThrow(now.toLocalDate() >= game.startDate.minusDays(ALLOWED_DAY_GAP)) {
            "게임 시작 ${ALLOWED_DAY_GAP}일 전부터 등록할 수 있습니다."
        }
    }

    private fun isAllowedTimeBefore(game: Game, now: LocalDateTime): Boolean {
        val gameStartDateTime = LocalDateTime.of(game.startDate, game.startTime)
        return gameStartDateTime > now.plusMinutes(ALLOWED_MINUTES_GAP)
    }

    companion object {
        private const val ALLOWED_MINUTES_GAP = 10L
        private const val ALLOWED_DAY_GAP = 2L
    }
}
