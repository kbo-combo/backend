package com.example.kbocombo.combo.domain

data class ComboSucceedEvent(
    val memberId: Long
)

data class ComboFailedEvent(
    val memberId: Long
)

data class ComboPassedEvent(
    val memberId: Long
)
