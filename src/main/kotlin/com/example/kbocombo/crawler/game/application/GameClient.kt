package com.example.kbocombo.crawler.game.application

import com.example.kbocombo.crawler.game.infra.GameDto
import java.time.LocalDate

interface GameClient {

    fun findGames(gameDate: LocalDate): List<GameDto>
}
