package com.example.kbocombo.record.application

import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.vo.WebId
import com.example.kbocombo.record.domain.HitterGameRecord
import com.example.kbocombo.record.infra.HitterGameRecordRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class HitterGameRecordService(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val hitterGameRecordRepository: HitterGameRecordRepository
) {

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun deleteAllHitterRecordByGame(event: GameDomainCanceledEvent) {
        hitterGameRecordRepository.deleteByGameId(event.gameId)
    }

    @Transactional
    fun saveOrUpdateHitterRecords(gameCode: String, playerRecordRequest: List<PlayerRequest>) {
        val game = gameRepository.findByGameCode(gameCode)
            ?: throw IllegalArgumentException("존재하지 않는 게임입니다.")
        if (game.isPending()) return

        val requestByWebId = playerRecordRequest.associateBy { it.webId }
        val hitterGameRecords = hitterGameRecordRepository.findAllByGameId(game.id)
            .associateBy { it.playerId }
        saveOrUpdate(requestByWebId, hitterGameRecords, game)
    }

    private fun saveOrUpdate(
        requestByWebId: Map<WebId, PlayerRequest>,
        hitterGameRecords: Map<Long, HitterGameRecord>,
        game: Game
    ) {
        val players = playerRepository.findAllByWebIdIn(requestByWebId.keys)
        players.forEach { player ->
            val request = requestByWebId[player.webId]!!
            hitterGameRecords[player.id]
                ?.let { updateHitterRecord(it, request) }
                ?: saveHitterRecord(game, player, request)
        }
    }

    private fun updateHitterRecord(hitterGameRecord: HitterGameRecord, request: PlayerRequest) {
        hitterGameRecord.updateStat(pa = request.pa, hit = hitterGameRecord.hit)
    }

    private fun saveHitterRecord(game: Game, player: Player, request: PlayerRequest) {
        hitterGameRecordRepository.save(
            HitterGameRecord(
                gameId = game.id,
                gameDate = game.startDate,
                playerId = player.id,
                pa = request.pa,
                hit = request.hit
            )
        )
    }
}


data class GameDomainCanceledEvent(val gameId: Long)

data class PlayerRequest(
    val webId: WebId,
    val pa: Int,
    val hit: Int
)
