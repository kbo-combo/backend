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

    atBats: Int,

    hits: Int,
) : AbstractAggregateRoot<HitterGameRecord>() {


    // 타수 AB
    @Column(name = "at_bats", nullable = false)
    var atBats: Int = atBats
        protected set

    // 안타
    @Column(name = "hits", nullable = false)
    var hits: Int = hits
        protected set

    @PostPersist
    fun publishHitterHitRecordEvent() {
        if (hits > 0) {
            registerHitterHitRecordEvent()
        }
    }

    fun updateStat(pa: Int, hit: Int) {
        this.atBats = pa
        if (this.hits == 0 && hit > 0) {
            registerHitterHitRecordEvent()
        }
        this.hits = hit
    }

    private fun registerHitterHitRecordEvent() {
        registerEvent(HitterHitRecordedEvent(playerId = playerId, gameId = gameId))
    }
}

data class HitterHitRecordedEvent(
    val playerId: Long,
    val gameId: Long,
)
