package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.Combo
import org.springframework.data.repository.Repository

interface ComboRepository : Repository<Combo, Long> {

    fun save(combo: Combo): Combo
}
