package com.example.kbocombo.record.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PostPersist
import org.springframework.data.domain.AbstractAggregateRoot
import java.time.LocalDate

@Entity(name = "HITTER_GAME_RECORD")
class HitterGameRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "game_id", nullable = false)
    val gameId: Long,

    @Column(name = "game_date", nullable = false)
    val gameDate: LocalDate,

    @Column(name = "player_id", nullable = false)
    val playerId: Long,

    pa: Int,

    hit: Int,
) : AbstractAggregateRoot<HitterGameRecord>() {

    @Column(name = "pa", nullable = false)
    var pa: Int = pa
        protected set

    @Column(name = "hit", nullable = false)
    var hit: Int = hit
        protected set

    @PostPersist
    fun publishHitterHitRecordEvent() {
        if (hit > 0) {
            registerHitterHitRecordEvent()
        }
    }

    fun updateStat(pa: Int, hit: Int) {
        this.pa = pa
        if (this.hit == 0 && hit > 0) {
            registerHitterHitRecordEvent()
        }
        this.hit = hit
    }

    private fun registerHitterHitRecordEvent() {
        registerEvent(HitterHitRecordedEvent(playerId = playerId, gameId = gameId))
    }
}

data class HitterHitRecordedEvent(
    val playerId: Long,
    val gameId: Long,
)
