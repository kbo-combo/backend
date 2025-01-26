package com.example.kbocombo.combo.ui

import com.example.kbocombo.combo.ui.request.ComboCreateRequest
import com.example.kbocombo.member.domain.Member
import com.example.kbocombo.member.ui.MemberResolver
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/combos")
class ComboController {

    @PostMapping
    fun createCombo(
        @MemberResolver member: Member,
        @RequestBody request: ComboCreateRequest,
    ) {

    }

    @DeleteMapping("/{comboId}")
    fun deleteCombo(
        @MemberResolver member: Member,
        @PathVariable comboId: Long,
    ) {
    }
}
