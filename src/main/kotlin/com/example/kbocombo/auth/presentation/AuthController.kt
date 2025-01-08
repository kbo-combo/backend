package com.example.kbocombo.auth.presentation

import com.example.kbocombo.auth.application.AuthService
import com.example.kbocombo.member.domain.vo.SocialProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Locale

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
    ): ResponseEntity<Unit> {
        val authorizedRedirectUri = authService.getRedirectUri(
            socialProvider = SocialProvider.valueOf(socialProvider.uppercase(Locale.getDefault())),
            redirectUri = redirectUri
        )
        response.sendRedirect(authorizedRedirectUri)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/oauth/{socialProvider}/login")
    fun login(
        @PathVariable socialProvider: String,
        @RequestParam code: String,
    ): ResponseEntity<Unit> {
        val memberInfo = authService.getMemberInfo(
            socialProvider = SocialProvider.valueOf(socialProvider.uppercase(Locale.getDefault())),
            code = code,
            redirectUri = "http://localhost:8080/oauth/kakao/login"
        )
        println(memberInfo.email + " " + memberInfo.nickname + " " + memberInfo.userId)
        return ResponseEntity.ok().build()
    }
}
