package com.example.kbocombo.crawler.utils

import com.example.kbocombo.domain.player.vo.Team

fun toTeamFilterCode(team: Team): String {
    return when (team) {
        Team.KIA -> "HT"
        Team.삼성 -> "SS"
        Team.LG -> "LG"
        Team.두산 -> "OB"
        Team.KT -> "KT"
        Team.SSG -> "SK"
        Team.롯데 -> "LT"
        Team.한화 -> "HH"
        Team.NC -> "NC"
        Team.키움 -> "WO"
    }
}
