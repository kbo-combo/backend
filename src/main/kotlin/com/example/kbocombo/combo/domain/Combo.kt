package com.example.kbocombo.combo.domain

import com.example.kbocombo.combo.domain.vo.ComboStatus
import com.example.kbocombo.common.BaseEntity
import com.example.kbocombo.game.domain.Game
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "COMBO")
class Combo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "member_id", nullable = false)
    val memberId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    var game: Game,

    @Column(name = "player_id", nullable = false)
    var playerId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "combo_status", nullable = false)
    var comboStatus: ComboStatus = ComboStatus.PENDING,
) : BaseEntity()
