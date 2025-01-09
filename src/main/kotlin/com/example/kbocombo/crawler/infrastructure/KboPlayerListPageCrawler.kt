package com.example.kbocombo.crawler.infrastructure

import com.example.kbocombo.domain.player.vo.PlayerPosition
import com.example.kbocombo.domain.player.vo.Team
import com.example.kbocombo.domain.player.vo.WebId
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap

@Component
class KboPlayerListPageCrawler(
    private val kboHttpClient: KboHttpClient,
    private val kboPlayerListPageParamGenerator: KboPlayerListPageParamGenerator,
    private val kboPlayerListPageParser: KboPlayerListPageParser
) {

    fun getPlayers(): List<WebPlayerInfo> {
        return Team.values()
            .flatMap { team -> getPlayers(team) }
    }

    private fun getPlayers(team: Team): List<WebPlayerInfo> {
        val response = getInitialResponse(team)

        return (1 until 15)
            .asSequence()
            .map { page -> getPlayersByPage(response, page, team) }
            .takeWhile { it.isNotEmpty() }
            .flatten()
            .toList()
    }

    private fun getInitialResponse(team: Team): ResponseEntity<String> {
        val initialResponse = kboHttpClient.getPlayers(LinkedMultiValueMap())
        val param = kboPlayerListPageParamGenerator.generateTeamFilterParam(initialResponse, team)
        return kboHttpClient.getPlayers(param)
    }

    private fun getPlayersByPage(
        response: ResponseEntity<String>,
        page: Int,
        team: Team
    ): List<WebPlayerInfo> {
        val pageParam = kboPlayerListPageParamGenerator.generatePageParam(response, page, team)
        val playerData = kboHttpClient.getPlayers(pageParam)
        return kboPlayerListPageParser.parse(playerData)
            .map { WebPlayerInfo(it.first, it.second, team) }
    }
}

data class WebPlayerInfo(
    val webId: WebId,
    val position: PlayerPosition,
    val team: Team
)
