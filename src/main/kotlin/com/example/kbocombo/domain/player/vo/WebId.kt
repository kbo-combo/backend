package com.example.kbocombo.domain.player.vo

/**
 * KBO 공식 사이트에서 쓰는 선수 식별자
 * 네이버도 이걸 식별자로 씀
 */
@JvmInline
value class WebId(
    val value: Long
) {

    constructor(value: String) : this(value.toLong())
}
