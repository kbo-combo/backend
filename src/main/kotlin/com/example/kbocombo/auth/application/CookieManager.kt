package com.example.kbocombo.auth.application

import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class CookieManager {

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
