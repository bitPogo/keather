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
import tech.antibytes.gradle.configuration.runtime.AntiBytesTestConfigurationTask
import tech.antibytes.gradle.configuration.sourcesets.iosx
import tech.antibytes.gradle.dependency.helper.nodeDevelopmentPackage
import tech.antibytes.gradle.dependency.helper.nodeProductionPackage
import tech.antibytes.gradle.project.config.database.SqlDelight

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.kotlinx.serialization)

    id(antibytesCatalog.plugins.square.sqldelight.get().pluginId)
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
                implementation(projects.data.location)
                implementation(projects.data.position)
                implementation(projects.interactor.repository)
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
        val androidMain by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk8)
                implementation(antibytesCatalog.android.ktor.client)
                implementation(antibytesCatalog.jvm.ktor.client.okhttp)
                implementation(antibytesCatalog.android.square.sqldelight.driver)
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

        val iosMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.square.sqldelight.driver.native)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)
                implementation(antibytesCatalog.js.square.sqldelight.driver)
                nodeProductionPackage(antibytesCatalog.node.sqlJs)
                nodeDevelopmentPackage(antibytesCatalog.node.copyWebpackPlugin)
                nodeProductionPackage(antibytesCatalog.node.sqlJsWorker)
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
    dependsOn(provideConfig, provideTestConfig)
    mustRunAfter(provideConfig, provideTestConfig)
}

tasks.withType(KotlinNativeCompile::class.java) {
    dependsOn(provideConfig, provideTestConfig)
    mustRunAfter(provideConfig, provideTestConfig)
}

tasks.withType(Kotlin2JsCompile::class.java) {
    dependsOn(provideConfig, provideTestConfig, prepareJsTest)
    mustRunAfter(provideConfig, provideTestConfig, prepareJsTest)
}

sqldelight {
    databases {
        create(SqlDelight.databaseName) {
            packageName.set("$projectPackage.database")
            srcDirs.setFrom("src/commonMain/database")
            generateAsync = true

            dependencies {
                dependency(projects.data.location)
            }
        }
    }
}
