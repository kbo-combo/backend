package com.example.kbocombo.game.domain.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class GameScore(
    @Column(name = "home_team_score")
    val homeTeamScore: Int,

    @Column(name = "away_team_score")
    val awayTeamScore: Int,
)
