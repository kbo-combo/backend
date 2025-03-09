package com.example.kbocombo.combo.domain

import com.example.kbocombo.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate

@Entity(name = "COMBO_RANK")
@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["years", "member_id"])
    ]
)
class ComboRank(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "years")
    val years: Int,

    @Column(name = "member_id")
    val memberId: Long,

    currentRecord: Int,
    successCount: Int,
    failCount: Int,
    passCount: Int,
    totalCount: Int,
    firstSuccessDate: LocalDate? = null,
    lastSuccessDate: LocalDate? = null
) : BaseEntity() {

    @Column(name = "current_record")
    var currentRecord: Int = currentRecord
        protected set

    @Column(name = "success_count")
    var successCount: Int = successCount
        protected set

    @Column(name = "fail_count")
    var failCount: Int = failCount
        protected set

    @Column(name = "pass_count")
    var passCount: Int = passCount
        protected set

    @Column(name = "total_count")
    var totalCount: Int = totalCount
        protected set

    @Column(name = "first_success_date")
    var firstSuccessDate: LocalDate? = firstSuccessDate
        protected set

    @Column(name = "last_success_date")
    var lastSuccessDate: LocalDate? = lastSuccessDate
        protected set


    fun recordComboSuccess(gameDate: LocalDate) {
        this.currentRecord += 1
        this.successCount += 1
        this.totalCount += 1
        if (this.firstSuccessDate == null) {
            this.firstSuccessDate = gameDate
        }
        this.lastSuccessDate = gameDate
    }

    fun recordComboFail() {
        this.currentRecord = 0
        this.failCount += 1
        this.totalCount += 1
        this.firstSuccessDate = null
        this.lastSuccessDate = null
    }

    fun recordComboPass() {
        this.passCount += 1
        this.totalCount += 1
    }

    companion object {
        fun init(memberId: Long, years: Int): ComboRank {
            return ComboRank(
                memberId = memberId,
                years = years,
                currentRecord = 0,
                successCount = 0,
                totalCount = 0,
                failCount = 0,
                passCount = 0,
                firstSuccessDate = null,
                lastSuccessDate = null
            )
        }
    }
}
