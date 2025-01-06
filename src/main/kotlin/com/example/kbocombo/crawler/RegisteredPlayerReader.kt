package com.example.kbocombo.crawler

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
    fun getPlayerWebIds(): List<Pair<WebId, PlayerPosition>> {
        val params = getInitialParams()
        return getPlayers(params)
    }

    private fun getInitialParams(): MultiValueMap<String, String> {
        val initialResponse = kboClient.getRegisteredPlayers(LinkedMultiValueMap())
        return registeredPlayerParameterGenerator.getParams(initialResponse)
    }

    private fun getPlayers(params: MultiValueMap<String, String>): List<Pair<WebId, PlayerPosition>> {
        return Team.entries
            .map { team ->
                val paramsWithTeam = registeredPlayerParameterGenerator.updateTeam(params, team)
                val response = kboClient.getRegisteredPlayers(paramsWithTeam)
                registeredPlayerParser.parse(response)
            }
            .flatten()
    }
}
