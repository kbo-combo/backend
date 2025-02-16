package com.example.kbocombo.crawler.player.infra

import com.example.kbocombo.crawler.common.application.KboHttpClient
import com.example.kbocombo.player.vo.PlayerPosition
import com.example.kbocombo.player.vo.Team
import com.example.kbocombo.player.vo.WebId
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap

/**
 * 해당 사이트의 프레임워크 특성상 이전 상태를 가지고 있어야 필터링이나 다음 페이지 이동등이 가능함
 * 따라서 다음과 같이 진행
 * 1. 최초 API 호출 (상태값 추가)
 * 2. Team Filter 적용
 * 3. 페이지 이동
 */
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
