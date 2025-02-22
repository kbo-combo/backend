package com.example.kbocombo.game.domain

import com.example.kbocombo.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate

@Entity(name = "GAME_END_EVENT_JOB")
class GameEndEventJob(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "game_id", nullable = false, updatable = false)
    val gameId: Long,

    @Column(name = "game_date", nullable = false, updatable = false)
    val gameDate: LocalDate,
) : BaseEntity() {

    @Column(name = "processed", nullable = false, updatable = false)
    var processed: Boolean = false
        protected set

    fun finish() {
        this.processed = true
    }
}
