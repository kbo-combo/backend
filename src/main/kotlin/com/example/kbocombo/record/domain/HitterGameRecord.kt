package com.example.kbocombo.record.domain

import com.example.kbocombo.common.AggregateBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PostPersist
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
) : AggregateBaseEntity<HitterGameRecord>() {

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

    fun updateStat(atBats: Int, hits: Int) {
        this.atBats = atBats
        if (this.hits == 0 && hits > 0) {
            registerHitterHitRecordEvent()
        }
        this.hits = hits
    }

    private fun registerHitterHitRecordEvent() {
        registerEvent(HitterHitRecordedEvent(playerId = playerId, gameId = gameId))
    }
}

data class HitterHitRecordedEvent(
    val playerId: Long,
    val gameId: Long,
)
