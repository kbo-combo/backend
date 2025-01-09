package com.example.kbocombo.player

import com.example.kbocombo.player.vo.HittingHandType
import com.example.kbocombo.player.vo.PitchingHandType
import com.example.kbocombo.player.vo.PlayerDetailPosition
import com.example.kbocombo.player.vo.PlayerDraftInfo
import com.example.kbocombo.player.vo.PlayerImage
import com.example.kbocombo.player.vo.PlayerPosition
import com.example.kbocombo.player.vo.Team
import com.example.kbocombo.player.vo.WebId
import jakarta.persistence.Column
import jakarta.persistence.Embedded
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

    @Embedded
    val playerDraftInfo: PlayerDraftInfo,

    @Embedded
    var playerImage: PlayerImage
) {

    @Column(name = "is_retired", nullable = false)
    var isRetired: Boolean = false
        protected set

    fun updateImageIfChanged(imageUrl: String?) {
        if (imageUrl == null) {
            return
        }

        if (playerImage.isDifferent(imageUrl)) {
            playerImage = PlayerImage(imageUrl)
        }
    }
}
