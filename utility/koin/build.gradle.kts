/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "tech.antibytes.keather.koin"
    compileSdk = 34
}

dependencies {
    implementation(antibytesCatalog.common.koin.core)
    api(antibytesCatalog.android.koin.androidBinding)

    testImplementation(antibytesCatalog.jvm.test.junit.core)
    testImplementation(platform(antibytesCatalog.jvm.test.junit.bom))
    testImplementation(antibytesCatalog.jvm.test.junit.runtime)
    testImplementation(antibytesCatalog.jvm.test.mockk)
    testImplementation(antibytesCatalog.jvm.test.kotlin.junit5)
    testImplementation(antibytesCatalog.testUtils.core)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()
}
