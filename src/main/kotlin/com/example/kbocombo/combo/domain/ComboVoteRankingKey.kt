package com.example.kbocombo.combo.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ComboVoteRankingKey {
    private const val PLAYER_COMBO_RANK_PREFIX = "player:combo:rank"
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * 특정 날짜의 선수 콤보 랭킹 키를 생성
     */
    fun playerComboRankByDate(gameDate: LocalDate): String {
        return "$PLAYER_COMBO_RANK_PREFIX:${gameDate.format(DATE_FORMATTER)}"
    }
} 
