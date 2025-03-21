package com.example.kbocombo.crawler.player.application

import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.infra.PlayerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PlayerRenewService(
    private val playerRepository: PlayerRepository
) {

    @Transactional
    fun renew(recentPlayers: List<Player>) {
        val savedPlayersByWebId = playerRepository.findAllByIsRetiredFalse()
            .associateBy { it.webId }

        for (recentPlayer in recentPlayers) {
            val savedPlayer = savedPlayersByWebId[recentPlayer.webId]
            if (savedPlayer == null) {
                playerRepository.save(recentPlayer)
                continue
            }

            savedPlayer.updateImageIfNeed(recentPlayer.playerImage)
        }
    }
}
