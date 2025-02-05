package com.example.kbocombo.member.ui

import com.example.kbocombo.auth.application.MemberSessionService
import com.example.kbocombo.auth.presentation.AuthController
import com.example.kbocombo.member.domain.Member
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.time.LocalDateTime

@Component
class MemberArgumentResolver(
    private val memberSessionService: MemberSessionService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(MemberResolver::class.java) &&
                parameter.parameterType == Member::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: throw IllegalStateException("인증 요청을 처리할 수 없습니다.")

        val sessionKey = request.cookies.find { it.name == AuthController.COOKIE_SESSION_KEY }?.value
            ?: throw IllegalStateException("세션 정보를 찾을 수 없습니다.")

        return memberSessionService.findMemberBySessionKey(sessionKey, LocalDateTime.now())
            ?: throw IllegalStateException("세션 정보를 찾을 수 없습니다.")
    }
}
