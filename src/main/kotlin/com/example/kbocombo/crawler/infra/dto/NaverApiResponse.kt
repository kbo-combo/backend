package com.example.kbocombo.crawler.infra.dto

data class NaverApiResponse<T>(
    val code: Int,
    val success: Boolean,
    val result: T
)
