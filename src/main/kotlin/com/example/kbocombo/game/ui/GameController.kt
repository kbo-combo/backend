package com.example.kbocombo.game.ui

import com.example.kbocombo.game.application.GameQueryService
import com.example.kbocombo.game.application.GameResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.Month
import java.time.Year

@RestController
@RequestMapping("/games")
class GameController(
    private val gameQueryService: GameQueryService
) {

    @GetMapping("/daily")
    fun findGamesByDate(@RequestParam gameDate: LocalDate): List<GameResponse> {
        return gameQueryService.findAllGamesByDate(gameDate)
    }

    @GetMapping("/games/{year}/{month}")
    fun findGamesByYearAndMonth(
        @PathVariable year: Year,
        @PathVariable month: Month
    ): List<GameResponse> {
        return listOf()
    }
}
