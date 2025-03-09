package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.ComboRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ComboRankRepository : JpaRepository<ComboRank, Long> {

    fun findByMemberId(memberId: Long): ComboRank
    fun save(comboRank: ComboRank): ComboRank
    fun findByMemberIdAndYear(memberId: Long, year: Int): ComboRank?
}
