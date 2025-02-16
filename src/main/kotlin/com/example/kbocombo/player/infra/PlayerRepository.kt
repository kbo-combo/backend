package com.example.kbocombo.player.infra

import com.example.kbocombo.player.Player
import com.example.kbocombo.player.vo.WebId
import org.springframework.data.repository.Repository

fun PlayerRepository.getById(playerId: Long): Player =
    findById(playerId) ?: throw IllegalArgumentException("존재하지 않는 선수입니다.")

interface PlayerRepository : Repository<Player, Long> {

    fun save(player: Player) : Player

    fun findAllByIsRetiredFalse() : List<Player>

    fun findById(playerId: Long): Player?

    fun findByWebId(webId: WebId) : Player?
}
