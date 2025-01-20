package com.example.kbocombo.player.presentation

import com.example.kbocombo.player.application.HitterQueryService
import com.example.kbocombo.player.presentation.request.HitterComboQueryRequest
import com.example.kbocombo.player.presentation.response.HitterComboResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hitters")
class HitterController(
    private val hitterQueryService: HitterQueryService,
) {

    @GetMapping
    fun findAllHittersByTeam(request: HitterComboQueryRequest): List<HitterComboResponse> {
        return hitterQueryService.findAllHittersByTeam(request)
    }
}
