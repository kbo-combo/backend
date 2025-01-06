package com.example.kbocombo.domain

@JvmInline
value class WebId(
    val value: Long
) {

    constructor(value: String) : this(value.toLong())
}
