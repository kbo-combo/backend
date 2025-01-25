package com.example.kbocombo.member.ui

import com.example.kbocombo.member.application.MemberService
import com.example.kbocombo.member.domain.Member
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @PutMapping("/members/nickname")
    fun changeNickname(
        @MemberResolver member: Member,
        @Valid @RequestBody nicknameChangeRequest: NicknameChangeRequest
    ): ResponseEntity<Unit> {
        memberService.updateNickname(
            memberId = member.id,
            nickname = nicknameChangeRequest.nickname!!
        )
        return ResponseEntity.ok().build()
    }
}

data class NicknameChangeRequest(
    @field:NotBlank(message = "닉네임은 빈 값이 될 수 없습니다.") val nickname: String? = null
)
