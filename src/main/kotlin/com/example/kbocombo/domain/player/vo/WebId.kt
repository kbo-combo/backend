package com.example.kbocombo.domain.player.vo

@JvmInline
value class WebId(
    val value: Long
) {

    constructor(value: String) : this(value.toLong())
}
