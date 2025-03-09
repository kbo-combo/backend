package com.example.kbocombo.combo.domain

data class ComboSucceedEvent(
    val memberId: Long,
    val comboId: Long
)

data class ComboFailedEvent(
    val memberId: Long,
    val comboId: Long
)

data class ComboPassedEvent(
    val memberId: Long,
    val comboId: Long
)
