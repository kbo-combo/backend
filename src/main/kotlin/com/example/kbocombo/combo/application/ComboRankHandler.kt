package com.example.kbocombo.combo.application

import com.example.kbocombo.auth.application.MemberSignupedEvent
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
    fun handleMemberSignupedEvent(memberSignupedEvent: MemberSignupedEvent) {
        logInfo("Handle MemberSignuped event, memberId = ${memberSignupedEvent.memberId}")
        comboRankService.create(memberId = memberSignupedEvent.memberId)
    }
}
