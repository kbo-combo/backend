package com.example.kbocombo.auth.application

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class CookieManager {

    fun getSessionKey(request: HttpServletRequest): String? {
        return request.cookies
            ?.find { it.name == CookieManager.COOKIE_SESSION_KEY }
            ?.value
    }

    fun generateSessionCookie(sessionKey: String) =
        ResponseCookie.from(COOKIE_SESSION_KEY, sessionKey)
            .httpOnly(true)
            .secure(true)
            .sameSite("none")
            .path("/")
            .build()

    companion object {
        const val COOKIE_SESSION_KEY = "JSESSIONID"
    }
}
