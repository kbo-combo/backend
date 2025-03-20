package com.example.kbocombo.combo.ui

import com.example.kbocombo.combo.application.ComboVoteRankingService
import com.example.kbocombo.combo.application.PlayerComboRankingResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class ComboVoteRankingController(
    private val comboVoteRankingService: ComboVoteRankingService
) {

    /**
     * 특정 날짜의 상위 N명 콤보 투표 랭킹 조회
     */
    @GetMapping("/combo-vote-ranking")
    fun getTopPlayerRanking(
        @RequestParam(required = false) gameDate: LocalDate? = LocalDate.now(),
        @RequestParam(defaultValue = "10") count: Long
    ): ResponseEntity<List<PlayerComboRankingResponse>> {
        val result = comboVoteRankingService.getTopRankedPlayersByDate(
            gameDate = gameDate ?: LocalDate.now(),
            count = count
        )
        return ResponseEntity.ok(result)
    }

    /**
     * 특정 날짜의 특정 선수 콤보 투표 랭킹 조회
     */
    @GetMapping("/combo-vote-ranking/players")
    fun getPlayerRanking(
        @RequestParam playerId: Long,
        @RequestParam gameDate: LocalDate = LocalDate.now()
    ): ResponseEntity<PlayerComboRankingResponse> {
        val result = comboVoteRankingService.getPlayerRankByDate(
            gameDate = gameDate,
            playerId = playerId
        )
        return ResponseEntity.ok(result)
    }
} 
