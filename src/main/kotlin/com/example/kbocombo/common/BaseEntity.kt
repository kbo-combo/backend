package com.example.kbocombo.common

import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
class BaseEntity(

    @CreatedDate
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    val updatedDateTime: LocalDateTime = LocalDateTime.now()
)
