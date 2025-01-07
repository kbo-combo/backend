package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.client.KboClient
import com.example.kbocombo.crawler.utils.getPlayerSearchTeamCode
import com.example.kbocombo.domain.player.vo.PlayerPosition
import com.example.kbocombo.domain.player.vo.Team
import com.example.kbocombo.domain.player.vo.WebId
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap

@Component
class PlayerSearchPageReader(
    private val kboClient: KboClient,
    private val playerSearchParamGenerator: PlayerSearchParamGenerator,
    private val playerSearchPageParser: PlayerSearchPageParser
) {
    fun findAll(): List<Pair<WebId, PlayerPosition>> {
        return Team.values()
            .flatMap { team -> getWebIdsByTeam(team) }
    }

    private fun getWebIdsByTeam(team: Team): List<Pair<WebId, PlayerPosition>> {
        val webIds = mutableListOf<Pair<WebId, PlayerPosition>>()
        val teamCode = getPlayerSearchTeamCode(team)
        val response = getInitialResponse(teamCode)

        for (page in 1 until 15) {
            val parsedWebIds = getWebIds(response, page, teamCode)
            if (parsedWebIds.isEmpty()) {
                return webIds
            }
            webIds.addAll(parsedWebIds)
        }

        return webIds
    }

    private fun getInitialResponse(teamCode: String): ResponseEntity<String> {
        val initialResponse = kboClient.getPlayers(LinkedMultiValueMap())
        val param = playerSearchParamGenerator.generateTeamFilterParam(initialResponse, teamCode)
        return kboClient.getPlayers(param)
    }

    private fun getWebIds(
        response: ResponseEntity<String>,
        page: Int,
        teamCode: String
    ): List<Pair<WebId, PlayerPosition>> {
        val pageParam = playerSearchParamGenerator.generatePageParam(response, page, teamCode)
        val playerData = kboClient.getPlayers(pageParam)
        return playerSearchPageParser.parse(playerData)
    }
}
