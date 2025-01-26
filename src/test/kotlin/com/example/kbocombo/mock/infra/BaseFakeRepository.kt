package com.example.kbocombo.mock.infra

import java.util.concurrent.atomic.AtomicLong
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

abstract class BaseFakeRepository<T : Any>(
    private val entityClass: KClass<T>,
    private val idFieldName: String = "id"
) {

    protected val db = mutableListOf<T>()
    private val idGenerator = AtomicLong(1)

    fun save(entity: T): T {
        val newEntity = if (getId(entity) == 0L) {
            val newId = idGenerator.getAndIncrement()
            setId(entity, newId)
        } else {
            entity
        }

        db.add(newEntity)
        return newEntity
    }

    private fun getId(entity: T): Long {
        val idProperty = entityClass.memberProperties.find { it.name == idFieldName }
        idProperty?.isAccessible = true
        return (idProperty?.getter?.call(entity) as? Long) ?: 0L
    }

    private fun setId(entity: T, newId: Long): T {
        val idField = entityClass.java.getDeclaredField(idFieldName)
        idField.isAccessible = true
        idField.set(entity, newId)
        return entity
    }
}
