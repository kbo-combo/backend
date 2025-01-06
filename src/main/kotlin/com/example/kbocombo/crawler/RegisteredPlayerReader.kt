package com.example.kbocombo.crawler

import com.example.kbocombo.crawler.RegisteredPlayerParameterGenerator.Companion.SELECT_TEAM_KEY
import com.example.kbocombo.entity.PlayerPosition
import com.example.kbocombo.entity.Team
import com.example.kbocombo.entity.WebId
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Component
class RegisteredPlayerReader(
    private val kboClient: KboClient,
    private val registeredPlayerParameterGenerator: RegisteredPlayerParameterGenerator,
    private val registeredPlayerParser: RegisteredPlayerParser
) {
    fun doSomething(): List<Pair<WebId, PlayerPosition>> {
        val initialResponse = kboClient.getRegisteredPlayers(LinkedMultiValueMap())
        val params = registeredPlayerParameterGenerator.getParams(initialResponse)
        return getPlayers(params)
    }

    private fun getPlayers(params: MultiValueMap<String, String>): List<Pair<WebId, PlayerPosition>> {
        val players = mutableListOf<Pair<WebId, PlayerPosition>>()
        for (team in Team.values()) {
            params[SELECT_TEAM_KEY] = WebTeamData.convert(team).name
            val response = kboClient.getRegisteredPlayers(params)
            players.addAll(registeredPlayerParser.parse(response))
        }
        return players
    }
}
