package com.example.kbocombo.crawler.game.application

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FunSpec
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime

@Ignored
@DisplayNameGeneration(ReplaceUnderscores::class)
@SpringBootTest
class GameSyncServiceTest(
    private val sut: GameSyncService
) : FunSpec({

    context("Game을 저장한다") {
        var date = LocalDate.parse("2025-03-08")
        val endDate = LocalDate.parse("2025-11-01")

        while (date.isBefore(endDate) || date.isEqual(endDate)) {
            sut.syncGame(gameDate = date, now = LocalDateTime.now())
        }
    }
})

