package com.example.kbocombo.player.presentation.response

import com.example.kbocombo.player.domain.Player
import com.example.kbocombo.player.vo.HittingHandType
import com.example.kbocombo.player.vo.PlayerDetailPosition
import com.example.kbocombo.player.vo.Team

data class HitterTeamResponse(
    val playerId: Long,
    val name: String,
    val team: Team,
    val detailPosition: PlayerDetailPosition,
    val hittingHandType: HittingHandType,
    val imageUrl: String?,
) {

    companion object {

        fun toList(players: List<Player>): List<HitterTeamResponse> {
            return players.map {
                HitterTeamResponse(
                    playerId = it.id,
                    name = it.name,
                    team = it.team,
                    detailPosition = it.detailPosition,
                    hittingHandType = it.hittingHandType,
                    imageUrl = it.playerImage.imageUrl
                )
            }
        }
    }
}
