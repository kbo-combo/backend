package com.example.kbocombo.exception

import com.example.kbocombo.exception.type.AuthenticationException
import com.example.kbocombo.exception.type.BadRequestException
import com.example.kbocombo.exception.type.InternalServerException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(AuthenticationException::class)
    fun handle(e: AuthenticationException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        return createResponse(HttpStatus.UNAUTHORIZED, e)
    }

    @ExceptionHandler(InternalServerException::class)
    fun handle(e: InternalServerException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handle(e: BadRequestException, request: HttpServletRequest): ResponseEntity<ExceptionResponse> {
        return createResponse(HttpStatus.BAD_REQUEST, e)
    }

    private fun createResponse(
        status: HttpStatus,
        e: Exception
    ) = ResponseEntity.status(status)
        .body(ExceptionResponse(e.message))
}

data class ExceptionResponse(
    val msg: String?
)
