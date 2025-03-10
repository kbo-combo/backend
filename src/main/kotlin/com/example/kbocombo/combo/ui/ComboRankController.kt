package com.example.kbocombo.combo.ui

import com.example.kbocombo.combo.application.ComboRankQueryService
import com.example.kbocombo.combo.application.ComboRankStatisticResponse
import com.example.kbocombo.combo.application.MemberComboRankResponse
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.ui.MemberResolver
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/combo-rank")
class ComboRankController(
    private val comboRankQueryService: ComboRankQueryService
) {

    @GetMapping("/detail")
    fun findOne(
        @MemberResolver member: Member,
        @RequestParam targetMemberId: Long,
    ): ResponseEntity<MemberComboRankResponse> {
        val response = comboRankQueryService.getMemberComboRank(memberId = targetMemberId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/statistic")
    fun findTopRankStatistic(
        @MemberResolver member: Member,
        @RequestParam year: Int = LocalDate.now().year,
        @RequestParam size: Int = 20
    ): ResponseEntity<ComboRankStatisticResponse> {
        val response = comboRankQueryService.getComboRankStatistic(year = year, count = size)
        return ResponseEntity.ok(response)
    }
}
