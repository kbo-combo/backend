package com.example.kbocombo.crawler.utils

import com.example.kbocombo.domain.Team

enum class RegisteredPageTeamConverter {
    HT, // 기아
    SS, // 삼성
    LG,
    OB,
    KT,
    SK,
    LT, // 롯데
    HH, // 한화
    NC,
    WO; // 키움

    companion object {
        fun convert(team: Team): RegisteredPageTeamConverter {
            return when (team) {
                Team.NC -> NC
                Team.KIA -> HT
                Team.두산 -> OB
                Team.LG -> LG
                Team.SSG -> SK
                Team.삼성 -> SS
                Team.롯데 -> LT
                Team.키움 -> WO
                Team.한화 -> HH
                Team.KT -> KT
            }
        }
    }
}
