package com.example.kbocombo.utils

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin

val fixture = FixtureMonkey.builder()
    .plugin(KotlinPlugin())
    .build()!!
