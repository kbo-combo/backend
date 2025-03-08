package com.example.kbocombo.combo.ui

import com.example.kbocombo.combo.application.ComboRankService
import com.example.kbocombo.combo.application.MemberComboRankResponse
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.ui.MemberResolver
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/combo-rank")
class ComboRankController(
    private val comboRankService: ComboRankService
) {

    @GetMapping("/detail")
    fun findOne(
        @MemberResolver member: Member,
        @RequestParam targetMemberId: Long,
    ): ResponseEntity<MemberComboRankResponse> {
        val response = comboRankService.getMemberComboRank(memberId = targetMemberId)
        return ResponseEntity.ok(response)
    }
}
