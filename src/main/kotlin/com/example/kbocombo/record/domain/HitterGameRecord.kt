package com.example.kbocombo.record.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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

    @Column(name = "pa", nullable = false)
    var pa: Int,

    @Column(name = "hit", nullable = false)
    var hit: Int,
) {
    fun updateStat(pa: Int, hit: Int) {
        this.pa = pa
        this.hit = hit
    }
}
