package com.example.kbocombo.crawler.service

import com.example.kbocombo.domain.player.Player
import com.example.kbocombo.domain.player.vo.WebId
import org.springframework.stereotype.Component

@Component
class PlayerSyncService(
    private val playerClient: PlayerClient,
    private val playerRepository: PlayerRepository,
) {

    fun synchronizePlayers() {
        val savedPlayers = playerRepository.findAll().associateBy { it.webId }
        val originPlayers = playerClient.findAllPlayers()
        synchronizePlayers(savedPlayers, originPlayers)
        updatePlayers(savedPlayers, originPlayers)
    }

    private fun synchronizePlayers(
        savedPlayers: Map<WebId, Player>,
        originPlayers: List<Player>
    ) {
        val newPlayers = originPlayers.filter { !savedPlayers.containsKey(it.webId) }
        newPlayers.forEach { playerRepository.save(it) }
    }

    private fun updatePlayers(savedPlayers: Map<WebId, Player>, originPlayers: List<Player>) {
        originPlayers
            .mapNotNull { originPlayer -> getImageRenewPlayers(savedPlayers, originPlayer) }
            .forEach { (player, newImageUrl) ->
                playerRepository.updateImage(player, newImageUrl)
            }
    }

    private fun getImageRenewPlayers(
        savedPlayers: Map<WebId, Player>,
        originPlayer: Player
    ): Pair<Player, String?>? {
        val savedPlayer = savedPlayers[originPlayer.webId] ?: return null

        return if (savedPlayer.isDifferentImageUrl(originPlayer.imageUrl)) {
            savedPlayer to originPlayer.imageUrl
        } else {
            null
        }
    }
}
