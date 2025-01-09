package com.example.kbocombo.crawler.application

import com.example.kbocombo.player.Player
import com.example.kbocombo.player.infra.PlayerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PlayerRenewService(
    private val playerRepository: PlayerRepository
) {

    @Transactional
    fun renew(recentPlayers: List<Player>) {
        val savedPlayers = playerRepository.findAll()
            .associateBy { it.webId }

        for (recentPlayer in recentPlayers) {
            val savedPlayer = savedPlayers[recentPlayer.webId]
            if (savedPlayer == null) {
                playerRepository.save(recentPlayer)
                continue
            }

            savedPlayer.updateImageIfChanged(recentPlayer.playerImage.imageUrl)
        }
    }
}