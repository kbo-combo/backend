package com.example.kbocombo.player.presentation.request

import com.example.kbocombo.player.vo.Team

data class HitterComboQueryRequest(
    val homeTeam: Team,
    val awayTeam: Team,
)
