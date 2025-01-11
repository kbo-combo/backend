package com.example.kbocombo.auth.presentation

import com.example.kbocombo.auth.application.AuthService
import com.example.kbocombo.auth.application.OAuthMemberResponse
import com.example.kbocombo.auth.application.OAuthRedirectUriResponse
import com.example.kbocombo.member.domain.vo.SocialProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Locale
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.UnknownContentTypeException

@RestController
@CrossOrigin(value = ["*"])
class AuthController(
    private val authService: AuthService
) {
    @GetMapping("/oauth/{socialProvider}")
    fun getAuthRedirectUri(
        @PathVariable socialProvider: String,
        @RequestParam redirectUri: String,
        response: HttpServletResponse
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
        val result = authService.getMemberInfo(
            socialProvider = SocialProvider.valueOf(socialProvider.uppercase(Locale.getDefault())),
            code = code,
            redirectUri = redirectUri
        )
        return ResponseEntity.ok().body(result)
    }
}
