package com.example.kbocombo.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(AuthenticationException::class)
    fun handle(e: AuthenticationException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        return createResponse(HttpStatus.UNAUTHORIZED, e)
    }

    private fun createResponse(
        status: HttpStatus,
        e: AuthenticationException
    ) = ResponseEntity.status(status)
        .body(ExceptionResponse.from(status, e))
}

class ExceptionResponse(
    val status: Int,
    val msg: String?
) {
    companion object {
        fun from(status: HttpStatus, e: Exception): ExceptionResponse {
            return ExceptionResponse(status.value(), e.message)
        }
    }
}
