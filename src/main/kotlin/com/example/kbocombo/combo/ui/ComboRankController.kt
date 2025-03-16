package com.example.kbocombo.combo.ui

import com.example.kbocombo.combo.application.ComboRankQueryService
import com.example.kbocombo.combo.application.MemberComboRankByYearResponse
import com.example.kbocombo.combo.application.TopComboRankResponse
import com.example.kbocombo.game.domain.vo.GameType
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
        @RequestParam targetMemberId: Long,
    ): ResponseEntity<List<MemberComboRankByYearResponse>> {
        val response = comboRankQueryService.getMemberComboRank(memberId = targetMemberId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/statistic")
    fun findTopRankStatistic(
        @RequestParam year: Int = LocalDate.now().year,
        @RequestParam size: Int = 20,
        @RequestParam gameType: GameType = GameType.REGULAR_SEASON,
        @RequestParam sort: String = "CURRENT_RECORD"
    ): ResponseEntity<TopComboRankResponse> {
        val response = comboRankQueryService.getComboRankStatistic(
            year = year,
            count = size,
            gameType = gameType,
            sort = sort
        )
        return ResponseEntity.ok(response)
    }
}
