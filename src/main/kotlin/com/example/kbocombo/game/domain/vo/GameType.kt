package com.example.kbocombo.game.domain.vo

import java.time.LocalDate

enum class GameType {
    PRE_SEASON,
    REGULAR_SEASON,
    POST_SEASON;

    companion object {

        private val REGULAR_SEASON_DATE = LocalDate.parse("2025-03-28")

        fun getGameTypeByDate(gameDate: LocalDate): GameType {
            require(gameDate.year == 2025) { "게임연도가 2025년이 아닙니다."}
            if (gameDate >= REGULAR_SEASON_DATE) {
                return REGULAR_SEASON
            }

            return PRE_SEASON
        }
    }
}
