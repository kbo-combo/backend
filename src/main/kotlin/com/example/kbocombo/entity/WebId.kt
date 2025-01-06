package com.example.kbocombo.entity

@JvmInline
value class WebId(
    val value: Long
) {

    constructor(value: String) : this(value.toLong())
}
