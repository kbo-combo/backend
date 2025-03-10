package com.example.kbocombo.game.infra

import com.example.kbocombo.game.domain.GameEndEventJob
import org.springframework.data.repository.Repository
import java.time.LocalDate

interface GameEndEventJobRepository : Repository<GameEndEventJob, Long> {

    fun save(gameEndEventJob: GameEndEventJob): GameEndEventJob

    fun findAllByProcessed(processed: Boolean): List<GameEndEventJob>

    fun findById(id: Long): GameEndEventJob

    fun findByGameId(gameId: Long): GameEndEventJob?
}
