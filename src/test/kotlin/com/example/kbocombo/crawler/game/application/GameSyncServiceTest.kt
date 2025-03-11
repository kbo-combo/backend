package com.example.kbocombo.crawler.game.application

import com.example.kbocombo.annotation.IntegrationTest
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FunSpec
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import java.time.LocalDate
import java.time.LocalDateTime

@Ignored
@DisplayNameGeneration(ReplaceUnderscores::class)
@IntegrationTest
class GameSyncServiceTest(
    private val sut: GameSyncService
) : FunSpec({

    context("Game을 저장한다") {
        var date = LocalDate.parse("2024-03-01")
        val endDate = LocalDate.parse("2025-10-02")

        while (date.isBefore(endDate) || date.isEqual(endDate)) {
            sut.syncGame(gameDate = date, now = LocalDateTime.now())
            date = date.plusDays(1)
        }
    }
})

