package com.example.kbocombo.auth.presentation

import com.example.kbocombo.auth.application.AuthService
import com.example.kbocombo.auth.application.OAuthMemberResponse
import com.example.kbocombo.auth.application.OAuthRedirectUriResponse
import com.example.kbocombo.member.domain.vo.SocialProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Locale


@RestController
//@CrossOrigin(origins = ["https://localhost:5173"], allowCredentials = "true")
class AuthController(
    private val authService: AuthService
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
        request: HttpServletRequest
    ): ResponseEntity<OAuthMemberResponse> {
        val memberResponse = authService.login(
            socialProvider = SocialProvider.valueOf(socialProvider.uppercase(Locale.getDefault())),
            code = code,
            redirectUri = redirectUri
        )

        setMemberSession(request, memberResponse)
        return ResponseEntity.ok().body(memberResponse)
    }

    private fun setMemberSession(
        request: HttpServletRequest,
        memberResponse: OAuthMemberResponse
    ) {
        val session = request.session
        session.setAttribute(SESSION_KEY, memberResponse.email)
        session.maxInactiveInterval = EXPIRED_TIME
    }

    companion object {
        private const val SESSION_KEY = "MEMBER_SESSION_KEY"
        private const val EXPIRED_TIME = 21600
    }
}
