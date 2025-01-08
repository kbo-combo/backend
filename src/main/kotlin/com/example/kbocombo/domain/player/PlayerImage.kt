package com.example.kbocombo.domain.player

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Table(name = "PLAYER_IMAGE")
@Entity
class PlayerImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    val player: Player,

    @Column(name = "image_url")
    private var imageUrl: String? = null,
) {

    fun getProfileImageUrl(): String? {
        return imageUrl
    }
}
