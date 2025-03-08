package com.example.kbocombo.combo.application

import com.example.kbocombo.auth.application.MemberSignupedEvent
import com.example.kbocombo.combo.domain.ComboFailedEvent
import com.example.kbocombo.combo.domain.ComboPassedEvent
import com.example.kbocombo.combo.domain.ComboSucceedEvent
import com.example.kbocombo.common.logInfo
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ComboRankHandler(
    private val comboRankService: ComboRankService
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handlerComboSucceedEvent(comboSucceedEvent: ComboSucceedEvent) {
        logInfo("Handle Combo succeed event, memberId = ${comboSucceedEvent.memberId}")
        comboRankService.recordSuccess(memberId = comboSucceedEvent.memberId)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handlerComboFailedEvent(comboFailedEvent: ComboFailedEvent) {
        logInfo("Handle Combo failed event, memberId = ${comboFailedEvent.memberId}")
        comboRankService.recordFail(memberId = comboFailedEvent.memberId)
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handlerComboPassedEvent(comboPassedEvent: ComboPassedEvent) {
        logInfo("Handle Combo passed event, memberId = ${comboPassedEvent.memberId}")
        comboRankService.recordPass(memberId = comboPassedEvent.memberId)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleMemberSignupedEvent(memberSignupedEvent: MemberSignupedEvent) {
        logInfo("Handle MemberSignuped event, memberId = ${memberSignupedEvent.memberId}")
        comboRankService.create(memberId = memberSignupedEvent.memberId)
    }
}
