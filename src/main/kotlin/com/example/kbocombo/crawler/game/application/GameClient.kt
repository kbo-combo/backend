package com.example.kbocombo.crawler.game.application

import com.example.kbocombo.game.domain.Game
import java.time.LocalDate

interface GameClient {

    fun findGames(gameDate: LocalDate) : List<Game>
}
