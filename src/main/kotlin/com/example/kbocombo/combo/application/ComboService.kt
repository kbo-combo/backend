package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.infra.ComboRepository
import com.example.kbocombo.combo.ui.request.ComboCreateRequest
import com.example.kbocombo.game.infra.GameRepository
import com.example.kbocombo.game.infra.getById
import com.example.kbocombo.member.domain.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ComboService(
    private val gameRepository: GameRepository,
    private val comboRepository: ComboRepository,
) {

    fun createCombo(
        member: Member,
        request: ComboCreateRequest,
        now: LocalDateTime
    ) {
        val game = gameRepository.getById(request.gameId)
        if (game.startDateTime.minusMinutes(10) < now) {
            throw IllegalArgumentException("게임 시작 10분 전에는 등록할 수 없습니다.")
        }
        comboRepository.save(Combo(
            memberId = member.id,
            gameId = game.id,
            playerId = request.playerId,
        ))
    }
}
