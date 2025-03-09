package com.example.kbocombo.annotation

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.annotation.AliasFor
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Transactional
@SpringBootTest
annotation class IntegrationTest(

    @get:AliasFor(annotation = SpringBootTest::class, attribute = "classes")
    val classes: Array<KClass<*>> = []
)

