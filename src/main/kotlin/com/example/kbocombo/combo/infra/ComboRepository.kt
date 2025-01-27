package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.Combo
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.time.LocalDateTime

fun ComboRepository.getById(comboId: Long): Combo =
    findById(comboId) ?: throw IllegalArgumentException("존재하지 않는 콤보입니다.")

interface ComboRepository : Repository<Combo, Long> {

    fun save(combo: Combo): Combo

    fun findById(comboId: Long): Combo?

    fun delete(combo: Combo)


    @Query(
        """
        select c
        from COMBO  c
        left join fetch c.game g
        where c.memberId = :memberId and g.startDateTime between :startDateTime and :endDateTime  
        """
    )
    fun findByMemberIdAndGameStartDateTime(memberId: Long, startDateTime: LocalDateTime, endDateTime: LocalDateTime): Combo?
}
