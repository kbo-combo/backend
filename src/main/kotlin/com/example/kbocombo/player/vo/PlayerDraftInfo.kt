package com.example.kbocombo.player.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class PlayerDraftInfo(
    @Column(name = "draft_detail", nullable = false)
    val draftDetail: String,

    @Column(name = "draft_year", nullable = false)
    val draftYear: Int,
)
