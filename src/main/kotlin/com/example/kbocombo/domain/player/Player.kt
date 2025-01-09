package com.example.kbocombo.domain.player

import com.example.kbocombo.domain.player.vo.HittingHandType
import com.example.kbocombo.domain.player.vo.PitchingHandType
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "birth_date", nullable = false)
    val birthDate: LocalDate,

    @Column(name = "height", nullable = false)
    val height: Int,

    @Column(name = "weight", nullable = false)
    val weight: Int,

    @Column(name = "draft_info", nullable = false)
    val draftInfo: String,

    @Column(name = "draft_year", nullable = false)
    val draftYear: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "hitting_hand_type", nullable = false)
    val hittingHandType: HittingHandType,

    @Enumerated(EnumType.STRING)
    @Column(name = "pitching_hand_type", nullable = false)
    val pitchingHandType: PitchingHandType,

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

    imageUrl: String?,
) {

    @Column(name = "is_retired", nullable = false)
    var isRetired: Boolean = false
        protected set

    @Column(name = "image_url", nullable = false)
    var imageUrl: String? = null
        protected set
}
