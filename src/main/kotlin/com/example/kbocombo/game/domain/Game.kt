package com.example.kbocombo.game.domain

import com.example.kbocombo.common.BaseEntity
import com.example.kbocombo.game.domain.vo.GameType
import com.example.kbocombo.player.vo.Team
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity(name = "GAME")
class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Enumerated(EnumType.STRING)
    @Column(name = "home_team", nullable = false)
    val homeTeam: Team,

    @Enumerated(EnumType.STRING)
    @Column(name = "away_team", nullable = false)
    val awayTeam: Team,

    @Column(name = "start_datetime", nullable = false)
    val startDateTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false)
    val gameType: GameType,
) : BaseEntity()
