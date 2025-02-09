package com.example.kbocombo.crawler.game.infra.dto

data class NaverApiResponse<T>(
    val code: Int,
    val success: Boolean,
    val result: T
)
