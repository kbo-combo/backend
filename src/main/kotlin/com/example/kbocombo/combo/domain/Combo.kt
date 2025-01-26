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
class Combo(
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

        if (now >= game.startDateTime.minusDays(2)) {
            throw IllegalArgumentException("게임 시작 2일 전부터 등록할 수 있습니다.")
        }

        if (game.startDateTime.minusMinutes(10) < now) {
            throw IllegalArgumentException("게임 시작 10분 전에는 등록할 수 없습니다.")
        }
    }
}
