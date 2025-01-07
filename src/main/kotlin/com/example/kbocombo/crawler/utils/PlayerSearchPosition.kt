package com.example.kbocombo.crawler.utils

import com.example.kbocombo.domain.player.vo.Team

fun toTeamFilterCode(team: Team): String {
    return when (team) {
        Team.KIA -> "HT"
        Team.SAMSUNG -> "SS"
        Team.LG -> "LG"
        Team.DOOSAN -> "OB"
        Team.KT -> "KT"
        Team.SSG -> "SK"
        Team.LOTTE -> "LT"
        Team.HANWHA -> "HH"
        Team.NC -> "NC"
        Team.KIWOOM -> "WO"
    }
}
