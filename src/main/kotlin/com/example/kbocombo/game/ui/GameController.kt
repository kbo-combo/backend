package com.example.kbocombo.game.ui

import com.example.kbocombo.game.application.GameQueryService
import com.example.kbocombo.game.application.GameResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/games")
class GameController(
    private val gameQueryService: GameQueryService
) {

    @GetMapping("/daily")
    fun findGameByDate(@RequestParam gameDate: LocalDate): List<GameResponse> {
        return gameQueryService.findByGameDate(gameDate)
    }
}
