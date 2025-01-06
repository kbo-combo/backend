package com.example.kbocombo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Table(name = "PLAYER")
@Entity
class PlayerEntity(
    @Column(name = "birth_date", nullable = false)
    var birthDate: LocalDate,
    @Column(name = "height")
    var height: Int?,
    @Column(name = "weight")
    var weight: Int?,
    @Column(name = "draft_info")
    var draftInfo: String?,
    @Column(name = "draft_year")
    var draftYear: Int?,
    @Column(name = "hitting_hand", nullable = false)
    var hittingHand: HandType,
    @Column(name = "throwing_hand", nullable = false)
    var throwingHand: HandType,
    @Column(name = "name", nullable = false)
    var name: String,
    @Column(name = "web_id", nullable = false)
    val webId: WebId,
    @Column(name = "position", nullable = false)
    val position: PlayerPosition,
    @Column(name = "team", nullable = false)
    var team: Team,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
)
