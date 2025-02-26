package com.example.kbocombo.game.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.crawler.game.infra.NaverSportHandler
import com.example.kbocombo.game.domain.GameEndEventJob
import com.example.kbocombo.game.infra.GameEndEventJobRepository
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class GameHandler(
    private val gameService: GameService,
    private val gameEndEventJobRepository: GameEndEventJobRepository,
    private val naverSportHandler: NaverSportHandler
) {

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleGameCompletedEvent(gameCompletedEvent: GameCompletedEvent) {
        val gameId = gameCompletedEvent.gameId
        val gameDate = gameCompletedEvent.gameDate
        logInfo("Game ended: $gameId ")

        gameService.complete(gameId)

        val gameEndEventJob = GameEndEventJob(
            gameId = gameId,
            gameDate = gameDate,
        )

        gameEndEventJobRepository.save(gameEndEventJob)
        naverSportHandler.run(gameId = gameId)
    }

    /**
     * @author gray
     * 경기가 진행 중일 때 gameScheduler에서 gameRunningEvnet 발행
     * 진행 중인 경기에서 안타를 찾도록 -> 콤보 체크
     */
    @Async
    @EventListener
    fun handleGameRunningEvent(gameRunningEvent: GameRunningEvent) {
        val gameId = gameRunningEvent.gameId
        logInfo("Game is Running: $gameId")

        gameService.run(gameId)
        naverSportHandler.run(gameId = gameId)
    }

    /**
     * @author gray
     * 네이버에서 제공하는 CANCEL의 개념이 우천취소, 폭염취소 등과 같은 노게임으로 선언되는 취소인 것 같음.
     * 그래서 취소가 결정됐을 때, 해당 게임의 콤보를 선택한 사람을 무효화할 수 있도록 GameEndEventJob 저장
     */
    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleGameCanceledEvent(gameCancelledEvent: GameCancelledEvent) {
        val gameId = gameCancelledEvent.gameId
        val gameDate = gameCancelledEvent.gameDate

        gameService.cancel(gameId)
        logInfo("Game is canceled: $gameId")

        val gameEndEventJob = GameEndEventJob(
            gameId = gameId,
            gameDate = gameDate,
        )

        gameEndEventJobRepository.save(gameEndEventJob)
    }
}


