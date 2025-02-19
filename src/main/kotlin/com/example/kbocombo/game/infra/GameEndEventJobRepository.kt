package com.example.kbocombo.game.infra

import com.example.kbocombo.game.domain.GameEndEventJob
import java.time.LocalDate
import org.springframework.data.repository.Repository

interface GameEndEventJobRepository : Repository<GameEndEventJob, Long> {

    fun save(gameEndEventJob: GameEndEventJob): GameEndEventJob

    fun findAllByProcessedAndGameDate(processed: Boolean, gameDate: LocalDate): List<GameEndEventJob>
}
