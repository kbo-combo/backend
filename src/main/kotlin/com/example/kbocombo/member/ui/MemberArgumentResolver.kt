package com.example.kbocombo.member.ui

import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.infra.MemberRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.lang.IllegalStateException

@Component
class MemberArgumentResolver(
    private val memberRepository: MemberRepository
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

        val session = request.getSession(false)
            ?: throw IllegalStateException("사용자 인증 정보를 찾을 수 없습니다.")

        val email = session.getAttribute(SESSION_KEY).toString()

        return memberRepository.findByEmail(email)
            ?: throw IllegalArgumentException("회원 정보를 찾을 수 없습니다. email: $email")
    }

    companion object {
        private const val SESSION_KEY = "MEMBER_SESSION_KEY"
    }
}
