package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.Combo
import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.game.domain.Game
import org.springframework.data.repository.Repository
import java.time.LocalDate

fun ComboRepository.getById(comboId: Long): Combo =
    findById(comboId) ?: throw IllegalArgumentException("존재하지 않는 콤보입니다.")

interface ComboRepository : Repository<Combo, Long> {

    fun save(combo: Combo): Combo

    fun findById(comboId: Long): Combo?

    fun delete(combo: Combo)

    fun findByMemberIdAndGameDate(memberId: Long, gameDate: LocalDate): Combo?

    fun findAllByGameAndPlayerIdAndComboStatus(game: Game, playerId: Long, comboStatus: ComboStatus): List<Combo>

    fun findAllByGameAndComboStatus(game: Game, comboStatus: ComboStatus): List<Combo>

    fun findAllByGame(game: Game): List<Combo>
}
