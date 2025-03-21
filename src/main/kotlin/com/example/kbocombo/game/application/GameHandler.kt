package com.example.kbocombo.game.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.crawler.game.application.HitterRecordClient
import com.example.kbocombo.game.domain.GameEndEventJob
import com.example.kbocombo.game.domain.vo.GameScore
import com.example.kbocombo.game.infra.GameEndEventJobRepository
import com.example.kbocombo.record.application.HitterGameRecordService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class GameHandler(
    private val gameService: GameService,
    private val gameEndEventJobRepository: GameEndEventJobRepository,
    private val hitterRecordClient: HitterRecordClient,
    private val hitterGameRecordService: HitterGameRecordService
) {

    @Async
    @Transactional
    fun completeGame(gameId: Long, gameScore: GameScore) {
        val game = gameService.complete(gameId)
        logInfo("Game completed: $gameId ")
        saveGameEndEventJob(gameId, game.startDate)
        updateHitterRecord(gameId)
    }

    /**
     * @author gray
     * 네이버 API 상에서 경기가 진행 중일 때 GameEntity Running 상태로 변경
     * 진행 중인 경기에서 안타를 찾도록 -> 콤보 체크
     */
    @Async
    @Transactional
    fun runGame(gameId: Long) {
        gameService.run(gameId)
        logInfo("Game is Running: $gameId")
        updateHitterRecord(gameId)
    }

    /**
     * @author gray
     * 네이버에서 제공하는 CANCEL의 개념이 우천취소, 폭염취소 등과 같은 노게임으로 선언되는 취소인 것 같음.
     * 그래서 취소가 결정됐을 때, 해당 게임의 콤보를 선택한 사람을 무효화할 수 있도록 GameEndEventJob 저장
     */
    @Async
    @Transactional
    fun cancelGame(gameId: Long) {
        val game = gameService.cancel(gameId)
        logInfo("Game is canceled: $gameId")
        saveGameEndEventJob(gameId, game.startDate)
    }

    private fun saveGameEndEventJob(gameId: Long, gameDate: LocalDate) {
        gameEndEventJobRepository.findByGameId(gameId)?.let {
            return
        }

        val gameEndEventJob = GameEndEventJob(gameId = gameId, gameDate = gameDate,)
        gameEndEventJobRepository.save(gameEndEventJob)
    }

    private fun updateHitterRecord(gameId: Long) {
        val hitterRecords = hitterRecordClient.findAll(gameId)
        hitterGameRecordService.saveOrUpdateHitterRecords(gameId, hitterRecords)
    }
}


