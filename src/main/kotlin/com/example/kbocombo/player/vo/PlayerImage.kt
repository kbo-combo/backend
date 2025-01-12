package com.example.kbocombo.player.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class PlayerImage(
    @Column(name = "image_url")
    val imageUrl: String?,
) {

    fun needsImageUpdate(newImageUrl: String?): Boolean {
        return !newImageUrl.isNullOrBlank() && this.imageUrl != newImageUrl
    }
}
