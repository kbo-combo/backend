package com.example.kbocombo.crawler.utils

import com.example.kbocombo.player.vo.HittingHandType
import com.example.kbocombo.player.vo.PitchingHandType
import com.example.kbocombo.player.vo.PlayerDetailPosition
import com.example.kbocombo.player.vo.Team

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

fun toPlayerDetailPosition(rawPosition: String): PlayerDetailPosition {
    return when (rawPosition) {
        "투수" -> PlayerDetailPosition.PITCHER
        "포수" -> PlayerDetailPosition.CATCHER
        "내야수" -> PlayerDetailPosition.IN_FIELDER
        "외야수" -> PlayerDetailPosition.OUT_FIELDER
        else -> error("Invalid rawPosition: $rawPosition")
    }
}

fun toHittingHand(rawHand: String): HittingHandType {
    return when (rawHand) {
        "좌타" -> HittingHandType.LEFT
        "우타" -> HittingHandType.RIGHT
        "양타" -> HittingHandType.SWITCH
        else -> error("Invalid HittingHand: $rawHand")
    }
}

fun toPitchingHand(rawHand: String): PitchingHandType {
    return when (rawHand) {
        "좌투" -> PitchingHandType.LEFT
        "우투" -> PitchingHandType.RIGHT
        "좌언" -> PitchingHandType.LEFT_SIDE
        "우언" -> PitchingHandType.RIGHT_SIDE
        else -> error("Invalid PitchingHandType: $rawHand")
    }
}
