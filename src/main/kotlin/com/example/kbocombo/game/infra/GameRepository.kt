package com.example.kbocombo.game.infra

import com.example.kbocombo.game.domain.Game
import java.time.LocalDate
import org.springframework.data.repository.Repository

interface GameRepository : Repository<Game, Long> {

    fun findAllByStartDate(startDate: LocalDate) : List<Game>
}
