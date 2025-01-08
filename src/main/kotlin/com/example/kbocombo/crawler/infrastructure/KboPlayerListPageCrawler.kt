package com.example.kbocombo.crawler.infrastructure

import com.example.kbocombo.crawler.client.KboClient
import com.example.kbocombo.crawler.dto.NewPlayerData
import com.example.kbocombo.crawler.service.PlayerCrawler
import com.example.kbocombo.domain.player.vo.Team
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap

@Component
class KboPlayerListPageCrawler(
    private val kboClient: KboClient,
    private val kboPlayerListPageParamGenerator: KboPlayerListPageParamGenerator,
    private val kboPlayerListPageParser: KboPlayerListPageParser
) : PlayerCrawler {

    override fun getPlayers(): List<NewPlayerData> {
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
        val param = kboPlayerListPageParamGenerator.generateTeamFilterParam(initialResponse, team)
        return kboClient.getPlayers(param)
    }

    private fun getPlayersByPage(
        response: ResponseEntity<String>,
        page: Int,
        team: Team
    ): List<NewPlayerData> {
        val pageParam = kboPlayerListPageParamGenerator.generatePageParam(response, page, team)
        val playerData = kboClient.getPlayers(pageParam)
        return kboPlayerListPageParser.parse(playerData)
            .map { NewPlayerData(it.first, it.second, team) }
    }
}
