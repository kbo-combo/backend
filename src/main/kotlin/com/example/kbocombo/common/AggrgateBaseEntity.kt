package com.example.kbocombo.common

import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AbstractAggregateRoot
import java.time.LocalDateTime

@MappedSuperclass
abstract class AggregateBaseEntity<T : AggregateBaseEntity<T>> : AbstractAggregateRoot<T>() {

    @CreatedDate
    val createdDateTime: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    val updatedDateTime: LocalDateTime = LocalDateTime.now()
}
