package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.client.KboClient
import com.example.kbocombo.crawler.utils.getPlayerSearchTeamCode
import com.example.kbocombo.domain.player.vo.PlayerPosition
import com.example.kbocombo.domain.player.vo.Team
import com.example.kbocombo.domain.player.vo.WebId
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Component
class PlayerSearchPageReader(
    private val kboClient: KboClient,
    private val playerSearchParamGenerator: PlayerSearchParamGenerator,
    private val playerSearchPageParser: PlayerSearchPageParser
) {
    fun findAll(): List<Pair<WebId, PlayerPosition>> {
        val players = mutableListOf<Pair<WebId, PlayerPosition>>()
        for (team in Team.values()) {
            players.addAll(findPlayersByTeam(team))
        }
        return players
    }

    private fun findPlayersByTeam(team: Team): List<Pair<WebId, PlayerPosition>> {
        val players = mutableListOf<Pair<WebId, PlayerPosition>>()
        val teamCode = getPlayerSearchTeamCode(team)
        var param = getInitialParam(teamCode)
        var teamFilterResponse = kboClient.getPlayers(param)

        for (page in 1 until 15) {
            param = playerSearchParamGenerator.generatePageParam(teamFilterResponse, page, teamCode)
            val data = kboClient.getPlayers(param)
            val elements = playerSearchPageParser.parse(data)
            if (elements.isEmpty()) {
                break
            }
            players.addAll(elements)
            teamFilterResponse = data
        }

        return players
    }

    private fun getInitialParam(teamCode: String): MultiValueMap<String, String> {
        val initialResponse = kboClient.getPlayers(LinkedMultiValueMap())
        return playerSearchParamGenerator.generateTeamFilterParam(initialResponse, teamCode)
    }
}
