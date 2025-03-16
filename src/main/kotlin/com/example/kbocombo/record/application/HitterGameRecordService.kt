package com.example.kbocombo.record.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.crawler.game.infra.dto.HitterRecordDto
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.vo.WebId
import com.example.kbocombo.record.domain.HitterGameRecord
import com.example.kbocombo.record.infra.HitterGameRecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HitterGameRecordService(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val hitterGameRecordRepository: HitterGameRecordRepository
) {

    @Transactional
    fun saveOrUpdateHitterRecords(gameId: Long, hitterRecordDtos: List<HitterRecordDto>) {
        val game = gameRepository.getById(gameId = gameId)
        if (game.isPending()) return

        val requestByWebId = hitterRecordDtos.associateBy { it.webId }
        val hitterGameRecords = hitterGameRecordRepository.findAllByGameId(game.id)
            .associateBy { it.playerId }
        saveOrUpdate(requestByWebId, hitterGameRecords, game)
    }

    @Transactional
    fun deleteAllHitterRecordIfGameCanceled(game: Game) {
        if (game.isCancelled().not()) {
            logInfo("game(gameId = ${game.id}) is not canceled")
            return
        }
        hitterGameRecordRepository.deleteAllByGameId(gameId = game.id)
    }

    private fun saveOrUpdate(
        requestByWebId: Map<WebId, HitterRecordDto>,
        hitterGameRecords: Map<Long, HitterGameRecord>,
        game: Game
    ) {
        val players = playerRepository.findAllByWebIdIn(requestByWebId.keys.map { it.value })
        players.forEach { player ->
            val request = requestByWebId[player.webId]!!
            hitterGameRecords[player.id]
                ?.let { updateHitterRecord(it, request) }
                ?: saveHitterRecord(game, player, request)
        }
    }

    private fun updateHitterRecord(hitterGameRecord: HitterGameRecord, request: HitterRecordDto) {
        hitterGameRecord.updateStat(atBats = request.atBats, hits = request.hits)
        hitterGameRecordRepository.save(hitterGameRecord)
    }

    private fun saveHitterRecord(game: Game, player: Player, request: HitterRecordDto) {
        hitterGameRecordRepository.save(
            HitterGameRecord(
                gameId = game.id,
                gameDate = game.startDate,
                playerId = player.id,
                atBats = request.atBats,
                hits = request.hits
            )
        )
    }
}
