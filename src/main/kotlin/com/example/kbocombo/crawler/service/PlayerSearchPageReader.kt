package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.client.KboClient
import com.example.kbocombo.crawler.utils.getPlayerSearchTeamCode
import com.example.kbocombo.domain.player.vo.PlayerPosition
import com.example.kbocombo.domain.player.vo.Team
import com.example.kbocombo.domain.player.vo.WebId
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap

@Component
class PlayerSearchPageReader(
    private val kboClient: KboClient,
    private val playerSearchParamGenerator: PlayerSearchParamGenerator,
    private val playerSearchPageParser: PlayerSearchPageParser
) {
    fun findAll(): List<Pair<WebId, PlayerPosition>> {
        val players = mutableListOf<Pair<WebId, PlayerPosition>>()
        for (team in Team.values()) {
            val initialResponse = kboClient.getPlayers(LinkedMultiValueMap())
            val teamCode = getPlayerSearchTeamCode(team)
            var param = playerSearchParamGenerator.generateTeamFilterParam(initialResponse, teamCode)
            var positionFilterResponse = kboClient.getPlayers(param)
            for (page in 1 until 10) {
                param = playerSearchParamGenerator.generatePageParam(positionFilterResponse, page, teamCode)
                val data = kboClient.getPlayers(param)
                val elements = playerSearchPageParser.parse(data)
                if (elements.isEmpty()) {
                    break
                }
                players.addAll(elements)
                positionFilterResponse = data
            }
        }

        return players
    }
}
