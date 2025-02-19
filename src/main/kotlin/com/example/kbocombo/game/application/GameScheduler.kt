package com.example.kbocombo.game.application

import com.example.kbocombo.game.infra.GameEndEventJobRepository
import java.time.LocalDateTime
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class GameScheduler(
    private val gameEndEventJobRepository: GameEndEventJobRepository,
    private val gameEndEventJobService: GameEndEventJobService,
) {

    @Scheduled(fixedDelay = 600000L)
    fun scheduleGameEndEventJob() {
        val now = LocalDateTime.now()
        val today = now.toLocalDate()

        val processableJobIds = gameEndEventJobRepository
            .findAllByProcessedAndGameDate(processed = false, gameDate = today)
            .filter { it.createdDateTime.isBefore(now.minusMinutes(10)) }
            .map { it.id }

        processableJobIds.forEach { gameEndEventJobService.process(it!!) }
    }
}
