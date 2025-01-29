package com.example.kbocombo.member.ui

import com.example.kbocombo.member.application.MemberQueryService
import com.example.kbocombo.member.application.MemberService
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.ui.response.MemberDetailResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
    private val memberQueryService: MemberQueryService
) {

    @GetMapping
    fun getMemberDetail(
        @MemberResolver member: Member,
    ): ResponseEntity<MemberDetailResponse> {
        val response = memberQueryService.getMemberDetail(memberId = member.id)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/nickname")
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
