package com.example.kbocombo.player.vo

enum class Team(
    val teamCode: String
) {
    NC("NC"),
    KIA("HT"),
    DOOSAN("OB"),
    LG("LG"),
    SSG("SK"),
    SAMSUNG("SS"),
    LOTTE("LT"),
    KIWOOM("WO"),
    HANWHA("HH"),
    KT("KT");

    companion object {

        fun fromTeamCode(teamCode: String): Team {
            return values().find { it.teamCode == teamCode }
                ?: throw IllegalArgumentException("존재하지 않는 TeamCode 입니다 teamCode = : $teamCode")
        }
    }
}

