package com.example.kbocombo.player.application

import com.example.kbocombo.player.infra.PlayerQueryRepository
import com.example.kbocombo.player.presentation.request.HitterComboQueryRequest
import com.example.kbocombo.player.presentation.response.HitterComboResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class PlayerQueryService(
    private val playerQueryRepository: PlayerQueryRepository
) {

    fun findAllHittersByTeam(request: HitterComboQueryRequest): List<HitterComboResponse> {
        val players = playerQueryRepository.findAllHitterByTeam(request)
        return HitterComboResponse.toList(players)
    }
}
