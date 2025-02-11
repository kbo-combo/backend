package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.infra.ComboQueryRepository
import com.example.kbocombo.combo.infra.ComboResponse
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ComboQueryService(
    private val comboQueryRepository: ComboQueryRepository
) {

    fun findByGameDate(gameDate: LocalDate): ComboResponse {
        return comboQueryRepository.findByGameDate(gameDate)
            ?: throw IllegalArgumentException("콤보가 존재하지 않습니다.")
    }
}
