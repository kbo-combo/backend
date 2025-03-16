package com.example.kbocombo.combo.domain

import com.example.kbocombo.game.domain.vo.GameType
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class ComboRankTest : ExpectSpec({

    context("최대 콤보 기록 테스트") {
        expect("최대 콤보 기록을 갱신한다.") {
            val comboRank = ComboRank.init(1L, 2025, GameType.REGULAR_SEASON)
            val gameDate = LocalDate.of(2025, 4, 1)

            comboRank.recordComboSuccess(gameDate = gameDate)
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(1))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(2))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(3))
            comboRank.recordComboFail()

            comboRank.currentRecord shouldBe 0
            comboRank.maxRecord shouldBe 4
        }

        expect("이전의 최대 기록보다 낮으면, 이전 최대 기록을 유지한다.") {
            val comboRank = ComboRank.init(1L, 2025, GameType.REGULAR_SEASON)
            val gameDate = LocalDate.of(2025, 4, 1)

            comboRank.recordComboSuccess(gameDate = gameDate)
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(1))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(2))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(3))
            comboRank.recordComboFail()
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(5))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(6))
            comboRank.recordComboFail()

            comboRank.currentRecord shouldBe 0
            comboRank.maxRecord shouldBe 4
        }

        expect("이전의 최대 기록보다 높으면, 새로운 최대 기록을 갱신한다.") {
            val comboRank = ComboRank.init(1L, 2025, GameType.REGULAR_SEASON)
            val gameDate = LocalDate.of(2025, 4, 1)

            comboRank.recordComboSuccess(gameDate = gameDate)
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(1))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(2))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(3))
            comboRank.recordComboFail()
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(5))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(6))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(7))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(8))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(9))

            comboRank.currentRecord shouldBe 5
            comboRank.maxRecord shouldBe 5
        }

        expect("기록 갱신 중 PASS 콤보가 발생해도 기록을 유지한다.") {
            val comboRank = ComboRank.init(1L, 2025, GameType.REGULAR_SEASON)
            val gameDate = LocalDate.of(2025, 4, 1)

            comboRank.recordComboSuccess(gameDate = gameDate)
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(1))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(2))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(3))
            comboRank.recordComboFail()
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(5))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(6))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(7))
            comboRank.recordComboPass()
            comboRank.recordComboPass()
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(8))
            comboRank.recordComboSuccess(gameDate = gameDate.plusDays(9))

            comboRank.currentRecord shouldBe 5
            comboRank.maxRecord shouldBe 5
        }
    }

})
