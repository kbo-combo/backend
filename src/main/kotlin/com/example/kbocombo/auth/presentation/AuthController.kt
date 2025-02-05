package com.example.kbocombo.auth.presentation

import com.example.kbocombo.auth.application.AuthService
import com.example.kbocombo.auth.application.MemberSessionResponse
import com.example.kbocombo.auth.application.MemberSessionService
import com.example.kbocombo.auth.application.OAuthMemberResponse
import com.example.kbocombo.auth.application.OAuthRedirectUriResponse
import com.example.kbocombo.member.domain.vo.SocialProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.Locale


@RestController
class AuthController(
    private val authService: AuthService,
    private val memberSessionService: MemberSessionService
) {
    @GetMapping("/oauth/{socialProvider}")
    fun getAuthRedirectUri(
        @PathVariable socialProvider: String,
        @RequestParam redirectUri: String,
    ): ResponseEntity<OAuthRedirectUriResponse> {
        val oAuthRedirectUriResponse = authService.getRedirectUri(
            socialProvider = SocialProvider.valueOf(socialProvider.uppercase(Locale.getDefault())),
            redirectUri = redirectUri
        )
        return ResponseEntity.ok().body(oAuthRedirectUriResponse)
    }

    @GetMapping("/oauth/{socialProvider}/login")
    fun login(
        @PathVariable socialProvider: String,
        @RequestParam code: String,
        @RequestParam redirectUri: String,
    ): ResponseEntity<OAuthMemberResponse> {
        val memberResponse = authService.login(
            socialProvider = SocialProvider.valueOf(socialProvider.uppercase(Locale.getDefault())),
            code = code,
            redirectUri = redirectUri
        )
        val sessionResponse = memberSessionService.saveSession(memberId = memberResponse.id, LocalDateTime.now())
        val cookie = generateCookie(sessionResponse)
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(memberResponse)
    }

    private fun generateCookie(sessionResponse: MemberSessionResponse) =
        ResponseCookie.from("JSESSIONID", sessionResponse.sessionKey)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .build()
}
