package com.example.kbocombo.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AbstractAggregateRoot
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AggregateBaseEntity<T : AggregateBaseEntity<T>> : AbstractAggregateRoot<T>() {

    @CreatedDate
    @Column(updatable = false)
    var createdDateTime: LocalDateTime = LocalDateTime.now()
        protected set
    @LastModifiedDate
    var updatedDateTime: LocalDateTime = LocalDateTime.now()
        protected set
}
