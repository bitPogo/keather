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
import tech.antibytes.gradle.dependency.helper.nodeDevelopmentPackage
import tech.antibytes.gradle.dependency.helper.nodeProductionPackage
import tech.antibytes.gradle.project.config.database.SqlDelight
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.api.VersioningConfiguration

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)

    alias(antibytesCatalog.plugins.square.sqldelight)
    alias(antibytesCatalog.plugins.kmock)
}

val projectPackage = "io.bitpogo.keather.data.position"

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

    iosx {
        val platform = if (name == "iosArm64") {
            "iphoneos"
        } else {
            "iphonesimulator"
        }
        val libraryName = "Locator"
        val libraryPath = "$rootDir/data/position/$libraryName/build/Build/Products/Release-$platform"
        val frameworksPath = libraryPath

        compilations.getByName("main") {
            cinterops.create("Locator") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":data:position:Locator:build${platform.capitalize()}")

                // Path to .def file
                defFile("$projectDir/src/nativeInterop/cinterop/Locator.def")
                includeDirs(libraryPath)
            }
        }

        compilations.getByName("test") {
            cinterops.create("Locator") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":data:position:Locator:build${platform.capitalize()}")

                // Path to .def file
                defFile("$projectDir/src/nativeInterop/cinterop/Locator.def")
                includeDirs.headerFilterOnly(libraryPath)
            }
        }

        binaries.all {
            linkerOpts(
                "-rpath", frameworksPath,
                "-L$libraryPath", "-l$libraryName",
                "-F$frameworksPath"
            )
        }
    }
    ensureAppleDeviceCompatibility()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.kotlin.stdlib)
                implementation(antibytesCatalog.common.kotlinx.coroutines.core)
                implementation(projects.entity)
                implementation(projects.interactor.repository)
            }
        }
        val commonTest by getting {
            kotlin {
                srcDir("build/generated/antibytes/commonMain/kotlin")
            }
            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)
                implementation(antibytesCatalog.testUtils.core)
                implementation(antibytesCatalog.testUtils.annotations)
                implementation(antibytesCatalog.testUtils.coroutine)
                implementation(antibytesCatalog.kfixture)
                implementation(antibytesCatalog.kmock)
                implementation(antibytesCatalog.common.square.sqldelight.primitiveAdapters)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(antibytesCatalog.android.google.android.playservice.location)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(antibytesCatalog.android.test.junit.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
                implementation(antibytesCatalog.android.test.ktx)
                implementation(antibytesCatalog.jvm.test.mockk)
                implementation(antibytesCatalog.android.test.robolectric)
                implementation(antibytesCatalog.android.square.sqldelight.driver)
            }
        }

        val iosTest by getting {
            dependencies {
                implementation(antibytesCatalog.common.square.sqldelight.driver.native)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)
                implementation(antibytesCatalog.js.kotlin.wrappers.browser)

            }
        }

        val jsTest by getting {
            dependencies {
                implementation(antibytesCatalog.js.test.kotlin.core)
                implementation(antibytesCatalog.js.square.sqldelight.driver)
                nodeProductionPackage(antibytesCatalog.node.sqlJs)
                nodeDevelopmentPackage(antibytesCatalog.node.copyWebpackPlugin)
                nodeProductionPackage(antibytesCatalog.node.sqlJsWorker)
                implementation(antibytesCatalog.js.test.kotlin.core)
            }
        }
    }
}

kmock {
    rootPackage = projectPackage
}

tasks.withType(Test::class.java) {
    testLogging {
        events(FAILED)
    }
}

sqldelight {
    databases {
        create(SqlDelight.databaseName) {
            packageName.set(projectPackage)
            srcDirs.setFrom("src/commonMain/database")
            generateAsync = true
        }
    }
}
