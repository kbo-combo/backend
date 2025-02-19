package com.example.kbocombo.game.ui

import com.example.kbocombo.game.application.GameByDateResponse
import com.example.kbocombo.game.application.GameQueryService
import com.example.kbocombo.game.application.GameYearMonthResponse
import org.springframework.http.ResponseEntity
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
    fun findGamesByDate(@RequestParam gameDate: LocalDate): ResponseEntity<List<GameByDateResponse>> {
        val response = gameQueryService.findAllGamesByDate(gameDate)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{year}/{month}")
    fun findGamesByYearAndMonth(
        @PathVariable year: Year,
        @PathVariable month: Int,
    ): ResponseEntity<List<GameYearMonthResponse>> {
        val response = gameQueryService.findAllByYearAndMonth(year = year, month = Month.of(month))
        return ResponseEntity.ok(response)
    }
}
