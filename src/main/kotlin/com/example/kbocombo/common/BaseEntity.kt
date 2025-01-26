package com.example.kbocombo.common

import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@MappedSuperclass
class BaseEntity(

    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    val updatedDate: LocalDateTime = LocalDateTime.now()
)
