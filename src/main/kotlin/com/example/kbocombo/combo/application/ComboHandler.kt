package com.example.kbocombo.combo.application

import com.example.kbocombo.common.logInfo
import com.example.kbocombo.crawler.infra.HitterHitRecordedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ComboHandler(
    private val comboService: ComboService
) {

    @Async
    @EventListener
    fun handleHitterHitRecordedEvent(hitterHitRecordedEvent: HitterHitRecordedEvent) {
        val gameId = hitterHitRecordedEvent.gameId
        val playerCode = hitterHitRecordedEvent.playerCode
        logInfo("Player code: $playerCode hits in game: $gameId ")

        comboService.updateComboToSuccess(gameId = gameId, playerWebId = playerCode.toLong())
    }

    /**
     * 경기 종료 트리거를 어떻게 할 지 생각해봐야 함.
     * 경기 종료가 되자 마자 실행하는 것보다, 일정 시간의 여유를 두고 실행하는게 좋을듯.
     * GameEndedEvent 클래스를 마땅히 둘 곳이 없어서, 우선 이 클래스에 생성합니다.
     * -> 추후 이벤트 발행하는 곳으로 옮겨야 함
     */
    @Async
    @EventListener
    fun handleGameEndedEvent(gameEndedEvent: GameEndedEvent) {
        val gameId = gameEndedEvent.gameId
        logInfo("Game ended: $gameId ")

        comboService.updateComboToFail(gameId = gameId)
    }
}

data class GameEndedEvent(val gameId: Long)
