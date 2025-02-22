package com.example.kbocombo.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory


fun Any.logInfo(message: String) {
    logger().info(message)
}

fun Any.logError(message: String? = null, e: Throwable) {
    logger().error(message ?: e.message ?: e.localizedMessage, e)
}

fun Any.logWarn(message: String? = null, e: Throwable) {
    logger().warn(message ?: e.message ?: e.localizedMessage, e)
}

private fun Any.logger(): Logger = LoggerFactory.getLogger(this.javaClass)
