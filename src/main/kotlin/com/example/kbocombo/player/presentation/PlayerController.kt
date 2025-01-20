package com.example.kbocombo.player.presentation

import com.example.kbocombo.player.application.PlayerQueryService
import com.example.kbocombo.player.presentation.request.HitterComboQueryRequest
import com.example.kbocombo.player.presentation.response.HitterComboResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/players")
class PlayerController(
    private val playerQueryService: PlayerQueryService,
) {

    @GetMapping
    fun findAllHittersByTeam(request: HitterComboQueryRequest): List<HitterComboResponse> {
        return playerQueryService.findAllHittersByTeam(request)
    }
}
