package com.example.kbocombo.combo.infra

import com.example.kbocombo.combo.domain.ComboVoteRankingKey
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@Repository
class ComboVoteRankingRepository(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    /**
     * 특정 날짜의 특정 선수에 대한 콤보 투표 증가
     */
    fun incrementPlayerComboVote(gameDate: LocalDate, playerId: Long, increment: Long = 1L) {
        val key = ComboVoteRankingKey.playerComboRankByDate(gameDate)
        val operations = redisTemplate.opsForZSet()
        operations.incrementScore(key, playerId.toString(), increment.toDouble())

        // 데이터 만료 설정 (30일)
        redisTemplate.expire(key, 30, TimeUnit.DAYS)
    }

    /**
     * 특정 날짜의 특정 선수에 대한 콤보 투표 감소
     */
    fun decrementPlayerComboVote(gameDate: LocalDate, playerId: Long) {
        val key = ComboVoteRankingKey.playerComboRankByDate(gameDate)
        val operations = redisTemplate.opsForZSet()

        val currentScore = operations.score(key, playerId.toString()) ?: 0.0
        if (currentScore <= 0.0) {
            return
        }

        val newScore = currentScore.minus(1)
        if (newScore <= 0) {
            operations.remove(key, playerId.toString())
        } else {
            operations.incrementScore(key, playerId.toString(), -1.0)
        }
    }

    /**
     * 특정 날짜의 콤보 투표 TOP N 선수 반환
     */
    fun getTopRankedPlayersByDate(gameDate: LocalDate, count: Long = 10): List<Pair<Long, Long>> {
        val key = ComboVoteRankingKey.playerComboRankByDate(gameDate)
        val operations = redisTemplate.opsForZSet()

        val rangeWithScores = operations.reverseRangeWithScores(key, 0, count - 1)

        return rangeWithScores?.map {
            Pair(it.value.toString().toLong(), (it.score ?: 0.0).toLong())
        } ?: emptyList()
    }

    /**
     * 특정 날짜의 특정 선수 콤보 투표 수 조회
     */
    fun getPlayerComboVoteCount(gameDate: LocalDate, playerId: Long): Long {
        val key = ComboVoteRankingKey.playerComboRankByDate(gameDate)
        val operations = redisTemplate.opsForZSet()
        return (operations.score(key, playerId.toString()) ?: 0.0).toLong()
    }

    /**
     * 특정 날짜의 특정 선수 콤보 투표 랭킹 조회 (1등부터 시작)
     */
    fun getPlayerRankByDate(gameDate: LocalDate, playerId: Long): Long {
        val key = ComboVoteRankingKey.playerComboRankByDate(gameDate)
        val operations = redisTemplate.opsForZSet()
        val rank = operations.reverseRank(key, playerId.toString())
        return if (rank != null) rank + 1 else 0L
    }
} 
