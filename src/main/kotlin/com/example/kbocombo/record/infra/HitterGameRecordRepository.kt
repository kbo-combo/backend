package com.example.kbocombo.record.infra

import com.example.kbocombo.record.domain.HitterGameRecord
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface HitterGameRecordRepository : Repository<HitterGameRecord, Long> {

    @Modifying
    @Query(
        """
            delete from HITTER_GAME_RECORD r
            where r.gameId = :gameId
        """
    )
    fun deleteAllByGameId(gameId: Long)

    fun save(gameRecord: HitterGameRecord): HitterGameRecord

    fun findAllByGameId(gameId: Long): List<HitterGameRecord>
}
