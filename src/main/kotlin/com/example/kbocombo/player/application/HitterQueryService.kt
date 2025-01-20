package com.example.kbocombo.player.application

import com.example.kbocombo.player.infra.HitterQueryRepository
import com.example.kbocombo.player.presentation.request.HitterComboQueryRequest
import com.example.kbocombo.player.presentation.response.HitterComboResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class HitterQueryService(
    private val hitterQueryRepository: HitterQueryRepository
) {

    fun findAllHittersByTeam(request: HitterComboQueryRequest): List<HitterComboResponse> {
        val players = hitterQueryRepository.findAllHitterByTeam(request)
        return HitterComboResponse.toList(players)
    }
}
