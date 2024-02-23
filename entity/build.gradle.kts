/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import tech.antibytes.gradle.configuration.sourcesets.nativeCoroutine

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)

    alias(antibytesCatalog.plugins.kmock)
}

val projectPackage = "io.keather.entity"

android {
    namespace = projectPackage
}

kotlin {
    androidTarget()

    jvm()

    js(IR) {
        compilations {
            this.forEach {
                it.compileTaskProvider.get().kotlinOptions.sourceMap = true
                it.compileTaskProvider.get().kotlinOptions.metaInfo = true

                if (it.name == "main") {
                    it.compileTaskProvider.get().kotlinOptions.main = "call"
                }
            }
        }

        browser {
            testTask {
                useKarma {
                    useChromeHeadlessNoSandbox()
                }
            }
        }
    }

    nativeCoroutine()
    ensureAppleDeviceCompatibility()

    sourceSets {
        val commonMain by getting
        val commonTest by getting

    }
}
