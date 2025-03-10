package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.ComboRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ComboRankRepository : JpaRepository<ComboRank, Long> {

    fun findAllByMemberId(memberId: Long): List<ComboRank>
    fun save(comboRank: ComboRank): ComboRank
    fun findByMemberIdAndYears(memberId: Long, years: Int): ComboRank?
}
