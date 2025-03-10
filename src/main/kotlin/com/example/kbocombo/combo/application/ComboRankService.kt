package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.domain.ComboRank
import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.combo.infra.ComboRankRepository
import com.example.kbocombo.combo.infra.ComboRepository
import com.example.kbocombo.common.logInfo
import com.example.kbocombo.game.domain.Game
import com.example.kbocombo.member.infra.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ComboRankService(
    private val memberRepository: MemberRepository,
    private val comboRepository: ComboRepository,
    private val comboRankRepository: ComboRankRepository,
) {

    @Transactional
    fun process(game: Game) {
        if (game.isRunning()) {
            logInfo("아직 게임이 완료되지 않았습니다. gameId: ${game.id}")
            return
        }

        val combos = comboRepository.findAllByGame(game)
        combos.forEach { recordToRank(it) }
    }

    private fun recordToRank(combo: Combo) {
        val comboRank = findComboRankOrSave(memberId = combo.memberId, year = combo.gameDate.year)

        when (combo.comboStatus) {
            ComboStatus.SUCCESS -> comboRank.recordComboSuccess(gameDate = combo.gameDate)
            ComboStatus.FAIL -> comboRank.recordComboFail()
            ComboStatus.PASS -> comboRank.recordComboPass()
            ComboStatus.PENDING -> {}
        }
        comboRankRepository.save(comboRank)
    }

    private fun findComboRankOrSave(memberId: Long, year: Int): ComboRank {
        return comboRankRepository.findByMemberIdAndYears(memberId = memberId, years = year)
            ?: comboRankRepository.save(
                ComboRank.init(
                    memberId = memberId, years = year
                )
            )
    }

    @Transactional
    fun create(memberId: Long) {
        val member = memberRepository.findById(memberId)

        val comboRank = ComboRank.init(
            memberId = member.id,
            years = LocalDate.now().year
        )
        comboRankRepository.save(comboRank)
    }
}
