package com.example.kbocombo.player.application

import com.example.kbocombo.player.infra.HitterQueryRepository
import com.example.kbocombo.player.presentation.request.HitterTeamRequest
import com.example.kbocombo.player.presentation.response.HitterTeamResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(readOnly = true)
class HitterQueryService(
    private val hitterQueryRepository: HitterQueryRepository
) {

    fun findAllHittersByTeam(request: HitterTeamRequest): List<HitterTeamResponse> {
        val players = hitterQueryRepository.findAllHitterByTeam(request)
        return HitterTeamResponse.toList(players)
    }
}
