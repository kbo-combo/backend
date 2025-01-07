package com.example.kbocombo.crawler.service

import com.example.kbocombo.crawler.client.KboClient
import com.example.kbocombo.crawler.dto.NewPlayerData
import com.example.kbocombo.domain.player.vo.Team
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap

@Component
class PlayerSearchPageReader(
    private val kboClient: KboClient,
    private val playerSearchParamGenerator: PlayerSearchParamGenerator,
    private val playerSearchPageParser: PlayerSearchPageParser
) {
    fun findAll(): List<NewPlayerData> {
        return Team.values()
            .flatMap { team -> getPlayers(team) }
    }

    private fun getPlayers(team: Team): List<NewPlayerData> {
        val response = getInitialResponse(team)

        return (1 until 15)
            .asSequence()
            .map { page -> getPlayersByPage(response, page, team) }
            .takeWhile { it.isNotEmpty() }
            .flatten()
            .toList()
    }

    private fun getInitialResponse(team: Team): ResponseEntity<String> {
        val initialResponse = kboClient.getPlayers(LinkedMultiValueMap())
        val param = playerSearchParamGenerator.generateTeamFilterParam(initialResponse, team)
        return kboClient.getPlayers(param)
    }

    private fun getPlayersByPage(
        response: ResponseEntity<String>,
        page: Int,
        team: Team
    ): List<NewPlayerData> {
        val pageParam = playerSearchParamGenerator.generatePageParam(response, page, team)
        val playerData = kboClient.getPlayers(pageParam)
        return playerSearchPageParser.parse(playerData)
            .map { NewPlayerData(it.first, it.second, team) }
    }
}
