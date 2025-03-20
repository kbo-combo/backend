package com.example.kbocombo.common.dto

data class SliceResponse<T> private constructor(
    val content: List<T>,
    val hasNext: Boolean
) {

    companion object {
        fun <T> of(content: List<T>, pageSize: Long): SliceResponse<T> {
            return SliceResponse(
                content = content.take(pageSize.toInt()),
                hasNext = content.size > pageSize
            )
        }
    }
}
