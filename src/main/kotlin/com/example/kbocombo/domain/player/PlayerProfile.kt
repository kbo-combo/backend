package com.example.kbocombo.domain.player

import com.example.kbocombo.domain.player.vo.Team
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "PLAYER_PROFILE")
@Entity
class PlayerProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, updatable = false)
    val player: Player,

    @Column(name = "team", nullable = false)
    val team: Team,

    @Column(name = "image_url")
    private var imageUrl: String? = null,

    @Column(name = "is_valid")
    var isValid: Boolean = true
) {

    fun getProfileImageUrl(): String? {
        return imageUrl
    }
}
