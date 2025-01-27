package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.Combo
import org.springframework.data.repository.Repository

fun ComboRepository.getById(comboId: Long): Combo =
    findById(comboId) ?: throw IllegalArgumentException("존재하지 않는 콤보입니다.")

interface ComboRepository : Repository<Combo, Long> {

    fun save(combo: Combo): Combo

    fun findById(comboId: Long): Combo?

    fun delete(combo: Combo)
}
