package com.example.kbocombo.combo.ui

import com.example.kbocombo.combo.application.ComboResponse
import com.example.kbocombo.combo.application.ComboService
import com.example.kbocombo.combo.application.request.ComboCreateRequest
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.ui.MemberResolver
import java.time.LocalDate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/combos")
class ComboController(
    private val comboService: ComboService
) {

    @PostMapping
    fun createCombo(
        @MemberResolver member: Member,
        @RequestBody request: ComboCreateRequest,
    ) {
        comboService.createCombo(
            request = request,
            memberId = member.id,
            now = LocalDateTime.now())
    }

    @DeleteMapping("/{comboId}")
    fun deleteCombo(
        @MemberResolver member: Member,
        @PathVariable comboId: Long,
    ) {
        comboService.deleteCombo(
            comboId = comboId,
            now = LocalDateTime.now())
    }

    @GetMapping
    fun readCombo(
        @MemberResolver member: Member,
        @RequestParam(required = false) gameId: Long?,
        @RequestParam(required = false) gameDate: LocalDate?,
    ): ResponseEntity<ComboResponse?> {
        val result = comboService.findCombo(
            member = member,
            gameId = gameId,
            gameDate = gameDate,
        )

        return ResponseEntity.ok(result)
    }
}
