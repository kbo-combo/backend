package com.example.kbocombo.combo.domain

import com.example.kbocombo.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate

@Entity(name = "COMBO_RANK")
class ComboRank(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "member_id", updatable = false, unique = true)
    val memberId: Long,

    @Column(name = "current_record")
    val currentRecord: Int,

    @Column(name = "success_count")
    val successCount: Int,

    @Column(name = "fail_count")
    val failCount: Int,

    @Column(name = "pass_count")
    val passCount: Int,

    @Column(name = "total_count")
    val totalCount: Int,

    @Column(name = "first_success_date")
    val firstSuccessDate: LocalDate,

    @Column(name = "last_success_date")
    val lastSuccessDate: LocalDate
) : BaseEntity() {
}
