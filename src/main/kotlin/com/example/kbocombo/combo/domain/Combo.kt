package com.example.kbocombo.combo.domain

import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.common.BaseEntity
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
    ) {
        checkCreate(game, now)
    }

    fun checkDelete(now: LocalDateTime) {
        require(isAllowedTimeBefore(game, now)) { "게임 시작 ${ALLOWED_MINUTES_GAP}분 이전에만 삭제할 수 있습니다." }
    }

    fun update(game: Game, playerId: Long, now: LocalDateTime) {
        require(isAllowedTimeBefore(game, now)) { "기존에 등록한 콤보가 게임 시작 ${ALLOWED_MINUTES_GAP}분 전 이라 삭제할 수 없습니다." }
        checkCreate(game, now)
        this.game = game
        this.playerId = playerId
    }

    private fun checkCreate(game: Game, now: LocalDateTime) {
        require(isAllowedTimeBefore(game, now)) {
            "게임 시작 ${ALLOWED_MINUTES_GAP}분 이전에만 등록할 수 있습니다."
        }

        require(now.toLocalDate() >= game.startDateTime.toLocalDate().minusDays(ALLOWED_DAY_GAP)) {
            "게임 시작 ${ALLOWED_DAY_GAP}일 전부터 등록할 수 있습니다."
        }
    }

    private fun isAllowedTimeBefore(game: Game, now: LocalDateTime) =
        game.startDateTime > now.plusMinutes(ALLOWED_MINUTES_GAP)

    companion object {
        private const val ALLOWED_MINUTES_GAP = 10L
        private const val ALLOWED_DAY_GAP = 2L
    }
}
