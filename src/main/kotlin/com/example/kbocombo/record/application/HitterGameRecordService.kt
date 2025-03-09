package com.example.kbocombo.record.application

import com.example.kbocombo.game.infra.GameRepository
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
        val requestByWebId = playerRecordRequest.associateBy { it.webId }
        val game = gameRepository.findByGameCode(gameCode)
            ?: throw IllegalArgumentException("존재하지 않는 게임입니다.")
        if (game.isPending()) {
            return
        }
        val players = playerRepository.findAllByWebIdIn(requestByWebId.keys)
        val hitterGameRecords = hitterGameRecordRepository.findAllByGameId(game.id)
            .associateBy { it.playerId }
        for (player in players) {
            val hitterGameRecord = hitterGameRecords[player.id]
            val request = requestByWebId[player.webId]!!
            if (hitterGameRecord == null) {
                hitterGameRecordRepository.save(HitterGameRecord(gameId = game.id,
                    gameDate = game.startDate,
                    playerId = player.id,
                    pa = request.pa,
                    hit = request.hit,
                    ))
                continue
            }
            hitterGameRecord.updateStat(pa = request.pa, hit = hitterGameRecord.hit)
        }
    }
}



data class GameDomainCanceledEvent(val gameId: Long)

data class PlayerRequest(
    val webId: WebId,
    val pa: Int,
    val hit: Int
)
