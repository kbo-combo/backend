package com.example.kbocombo.domain.player

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "PLAYER_IMAGE")
@Entity
class PlayerImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "image_url")
    private var imageUrl: String? = null,

    @Column(name = "player_id")
    val playerId: Long
) {

    fun getProfileImageUrl(): String? {
        return imageUrl
    }
}
