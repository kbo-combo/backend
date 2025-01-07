package com.example.kbocombo.domain.player

import com.example.kbocombo.domain.player.vo.HandType
import com.example.kbocombo.domain.player.vo.PlayerDetailPosition
import com.example.kbocombo.domain.player.vo.PlayerPosition
import com.example.kbocombo.domain.player.vo.Team
import com.example.kbocombo.domain.player.vo.WebId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Table(name = "PLAYER")
@Entity
class Player(
    @Column(name = "birth_date", nullable = false)
    val birthDate: LocalDate,

    @Column(name = "height")
    var height: Int?,

    @Column(name = "weight")
    var weight: Int?,

    @Column(name = "draft_info")
    var draftInfo: String?,

    @Column(name = "draft_year")
    var draftYear: Int?,

    @Enumerated(EnumType.STRING)
    @Column(name = "hitting_hand", nullable = false)
    val hittingHand: HandType,

    @Enumerated(EnumType.STRING)
    @Column(name = "throwing_hand", nullable = false)
    val throwingHand: HandType,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "web_id", nullable = false)
    val webId: WebId,

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    val position: PlayerPosition,

    @Enumerated(EnumType.STRING)
    @Column(name = "detail_position", nullable = false)
    val detailPosition: PlayerDetailPosition,

    @Enumerated(EnumType.STRING)
    @Column(name = "team", nullable = false)
    val team: Team,

    @Column(name = "retire", nullable = false)
    var retire: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
)
