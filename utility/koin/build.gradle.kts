/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
}

android {
    namespace = "io.bitpogo.keather.koin"
}

dependencies {
    implementation(antibytesCatalog.common.koin.core)
    api(antibytesCatalog.android.koin.androidBinding)

    testImplementation(antibytesCatalog.jvm.test.junit.core)
    testImplementation(antibytesCatalog.jvm.test.mockk)
    testImplementation(antibytesCatalog.jvm.test.kotlin.junit5)
    testImplementation(antibytesCatalog.testUtils.core)
}
