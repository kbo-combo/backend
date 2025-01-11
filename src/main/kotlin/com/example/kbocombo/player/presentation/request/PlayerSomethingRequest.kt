package com.example.kbocombo.domain.player.presentation.request

import com.example.kbocombo.player.vo.Team


data class PlayerSomethingRequest(
    val homeTeam: Team,
    val awayTeam: Team,
)
