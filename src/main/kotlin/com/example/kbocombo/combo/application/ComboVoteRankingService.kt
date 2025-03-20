package com.example.kbocombo.combo.application

import com.example.kbocombo.combo.infra.ComboVoteRankingRepository
import com.example.kbocombo.player.infra.PlayerRepository
import com.example.kbocombo.player.infra.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ComboVoteRankingService(
    private val comboVoteRankingRepository: ComboVoteRankingRepository,
    private val playerRepository: PlayerRepository
) {

    /**
     * 특정 선수에 대한 콤보 투표 증가
     */
    @Transactional
    fun incrementPlayerComboVote(gameDate: LocalDate, playerId: Long) {
        val player = playerRepository.getById(playerId)

        comboVoteRankingRepository.incrementPlayerComboVote(gameDate = gameDate, playerId = player.id)
    }

    /**
     * 특정 선수에 대한 콤보 투표 감소
     */
    @Transactional
    fun decrementPlayerComboVote(gameDate: LocalDate, playerId: Long) {
        val player = playerRepository.getById(playerId)

        comboVoteRankingRepository.decrementPlayerComboVote(gameDate = gameDate, playerId = player.id)
    }

    /**
     * 특정 날짜의 콤보 투표 TOP N 선수 조회
     */
    @Transactional(readOnly = true)
    fun getTopRankedPlayersByDate(gameDate: LocalDate, count: Long = 10): List<PlayerComboRankingResponse> {
        val rankedPlayers = comboVoteRankingRepository.getTopRankedPlayersByDate(gameDate, count)
        
        if (rankedPlayers.isEmpty()) {
            return emptyList()
        }
        
        val playerIds = rankedPlayers.map { it.first }
        val playerMap = playerRepository.findAllByIdIn(playerIds)
            .associateBy { it.id }
        
        return rankedPlayers.mapIndexed { index, (playerId, voteCount) ->
            val player = playerMap[playerId]

            PlayerComboRankingResponse(
                rank = (index + 1).toLong(),
                playerId = playerId,
                playerName = player!!.name,
                teamName = player.team.name,
                voteCount = voteCount,
                imageUrl = player.playerImage.imageUrl
            )
        }
    }
    
    /**
     * 특정 날짜의 특정 선수 콤보 투표 랭킹 조회
     */
    @Transactional(readOnly = true)
    fun getPlayerRankByDate(gameDate: LocalDate, playerId: Long): PlayerComboRankingResponse {
        val rank = comboVoteRankingRepository.getPlayerRankByDate(gameDate, playerId)
        val voteCount = comboVoteRankingRepository.getPlayerComboVoteCount(gameDate, playerId)
        
        val player = playerRepository.getById(playerId)
        
        return PlayerComboRankingResponse(
            rank = rank,
            playerId = playerId,
            playerName = player.name,
            teamName = player.team.name,
            voteCount = voteCount,
            imageUrl = player.playerImage.imageUrl
        )
    }
}

data class PlayerComboRankingResponse(
    val rank: Long,
    val playerId: Long,
    val playerName: String,
    val teamName: String,
    val imageUrl: String?,
    val voteCount: Long
)
