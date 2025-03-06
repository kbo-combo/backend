package com.example.kbocombo.game.domain.vo

import java.time.LocalDate

enum class GameType {
    PRE_SEASON,
    REGULAR_SEASON,
    POST_SEASON;

    companion object {

        private val REGULAR_SEASON_DATE = LocalDate.parse("2025-03-22")

        fun getGameTypeByDate(gameDate: LocalDate): GameType {
            if (gameDate >= REGULAR_SEASON_DATE) {
                return REGULAR_SEASON
            }

            return PRE_SEASON
        }
    }
}
