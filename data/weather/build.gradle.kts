/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.jetbrains.kotlin.gradle.model.SourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTestFramework
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask
import tech.antibytes.gradle.configuration.runtime.AntiBytesTestConfigurationTask
import tech.antibytes.gradle.configuration.sourcesets.iosx

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.kotlinx.serialization)

    alias(antibytesCatalog.plugins.kmock)
}

val projectPackage = "io.bitpogo.keather.data.weather"

android {
    namespace = projectPackage

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

kotlin {
    androidTarget()

    js(IR) {
        compilations {
            forEach {
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

    iosx()
    ensureAppleDeviceCompatibility()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            }
        }

        val commonMain by getting {
            kotlin.srcDir("${layout.buildDirectory.get().asFile.absolutePath.trimEnd('/')}/generated/antibytes/commonMain/kotlin")
            dependencies {
                implementation(antibytesCatalog.common.kotlin.stdlib)
                implementation(antibytesCatalog.common.kotlinx.coroutines.core)
                implementation(antibytesCatalog.common.koin.core)

                implementation(antibytesCatalog.common.ktor.client.core)
                implementation(antibytesCatalog.common.ktor.client.contentNegotiation)
                implementation(antibytesCatalog.common.ktor.serialization.json)

                implementation(antibytesCatalog.common.kotlinx.serialization.core)
                implementation(antibytesCatalog.common.kotlinx.serialization.json)

                implementation(antibytesCatalog.common.kotlinx.dateTime)

                implementation(projects.entity)
                implementation(projects.utility.http)
            }
        }
        val commonTest by getting {
            kotlin.srcDir("${layout.buildDirectory.get().asFile.absolutePath.trimEnd('/')}/generated/antibytes/commonTest/kotlin")
            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)

                implementation(antibytesCatalog.kmock)
                implementation(antibytesCatalog.kfixture)
                implementation(antibytesCatalog.testUtils.core)
                implementation(antibytesCatalog.testUtils.annotations)
                implementation(antibytesCatalog.testUtils.coroutine)
                implementation(antibytesCatalog.testUtils.ktor)
                implementation(antibytesCatalog.testUtils.resourceloader)
            }
        }
        val commonIntegrationTest by creating

        val androidMain by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk8)
                implementation(antibytesCatalog.android.ktor.client)
                implementation(antibytesCatalog.jvm.ktor.client.okhttp)
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

        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)
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

val provideConfig by tasks.creating(AntiBytesMainConfigurationTask::class.java) {
    val apiKey = System.getenv("WEATHER_API") ?: throw StopExecutionException("Missing WeatherApi Key")
    packageName.set("$projectPackage.config")
    stringFields.set(
        mapOf(
            "apiKey" to apiKey
        )
    )

    mustRunAfter("clean")
}

val provideTestConfig by tasks.creating(AntiBytesTestConfigurationTask::class.java) {
    mustRunAfter("clean")
    packageName.set("$projectPackage.config")
    stringFields.set(
        mapOf(
            "projectDir" to project.projectDir.absolutePath
        )
    )

    mustRunAfter("clean")
}

val prepareJsTest by tasks.creating(Copy::class.java) {
    val target = File(
        rootProject.layout.buildDirectory.get().asFile,
        "js/packages/keather-data-weather-test/src"
    )

    from(File(projectDir, "src"))
    into(target)
    include("**/resources/**", "**/res/**")
}

tasks.withType(Test::class.java) {
    testLogging {
        events(FAILED)
    }
}

tasks.withType(KotlinCompile::class.java) {
    dependsOn(provideConfig, provideTestConfig, prepareJsTest)
    mustRunAfter(provideConfig, provideTestConfig, prepareJsTest)
}

tasks.withType(KotlinNativeCompile::class.java) {
    dependsOn(provideConfig, provideTestConfig)
    mustRunAfter(provideConfig, provideTestConfig)
}

tasks.withType(Kotlin2JsCompile::class.java) {
    dependsOn(provideConfig, provideTestConfig)
    mustRunAfter(provideConfig, provideTestConfig)
}
