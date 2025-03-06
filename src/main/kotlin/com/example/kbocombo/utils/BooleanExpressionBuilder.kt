package com.example.kbocombo.utils

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.SimpleExpression
import com.querydsl.core.types.dsl.StringExpression
import com.querydsl.core.types.dsl.TemporalExpression


fun contains(expression: StringExpression, value: String?): BooleanExpression? {
    return if (value != null) expression.contains(value) else null
}

fun <T> eq(expression: SimpleExpression<T>, value: T?): BooleanExpression? {
    return if (value != null) expression.eq(value) else null
}

fun <T : Comparable<*>?> after(expression: TemporalExpression<T>, start: T?): BooleanExpression? {
    return if (start != null) expression.after(start) else null
}

fun <T : Comparable<*>?> before(expression: TemporalExpression<T>, before: T?): BooleanExpression? {
    return if (before != null) expression.before(before) else null
}

fun <T : Comparable<*>?> between(expression: TemporalExpression<T>, from: T?, to: T?): BooleanExpression? {
    if (from == null || to == null) return null
    return expression.between(from, to)
}
