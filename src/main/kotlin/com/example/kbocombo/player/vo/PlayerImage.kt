package com.example.kbocombo.player.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class PlayerImage(
    @Column(name = "image_url")
    val imageUrl: String?,
) {

    fun isDifferent(imageUrl: String): Boolean = this.imageUrl != imageUrl
}
