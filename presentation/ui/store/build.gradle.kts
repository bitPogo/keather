/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask
import tech.antibytes.gradle.configuration.sourcesets.iosx
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.api.VersioningConfiguration

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)

    alias(antibytesCatalog.plugins.kmock)
}

val projectPackage = "tech.antibytes.keather.presentation.ui.store"

android {
    namespace = projectPackage
}

kotlin {
    androidTarget()

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

        browser()
    }
    iosx()
    ensureAppleDeviceCompatibility()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.kotlinx.coroutines.core)
                api(projects.entity)
            }
        }

        val commonTest by getting {
            kotlin.srcDir("${layout.buildDirectory.get().asFile.absolutePath.trimEnd('/')}/generated/antibytes/commonTest/kotlin")
            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)
                implementation(antibytesCatalog.kfixture)
                implementation(antibytesCatalog.testUtils.core)
                implementation(antibytesCatalog.testUtils.annotations)
                implementation(antibytesCatalog.kmock)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(antibytesCatalog.android.test.junit.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
                implementation(antibytesCatalog.android.test.ktx)
                implementation(antibytesCatalog.android.test.robolectric)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(antibytesCatalog.js.test.kotlin.core)
            }
        }
    }
}

kmock {
    rootPackage = projectPackage
}
