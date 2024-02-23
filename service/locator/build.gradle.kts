/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import tech.antibytes.gradle.configuration.sourcesets.iosx

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)

    alias(antibytesCatalog.plugins.kmock)
}

val projectPackage = "io.bitpogo.keather.locator"

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

    iosx {
        val platform = if (name == "iosArm64") {
            "iphoneos"
        } else {
            "iphonesimulator"
        }
        val libraryName = "Locator"
        val libraryPath = "$rootDir/service/locator/$libraryName/build/Build/Products/Release-$platform"
        val frameworksPath = libraryPath

        println(libraryPath)

        compilations.getByName("main") {
            cinterops.create("Locator") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":service:locator:Locator:build${platform.capitalize()}")

                // Path to .def file
                defFile("$projectDir/src/nativeInterop/cinterop/Locator.def")
                includeDirs(libraryPath)
            }
        }

        compilations.getByName("test") {
            cinterops.create("Locator") {
                val interopTask = tasks[interopProcessingTaskName]
                interopTask.dependsOn(":service:locator:Locator:build${platform.capitalize()}")

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
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)
                implementation(antibytesCatalog.testUtils.core)
                implementation(antibytesCatalog.testUtils.annotations)
                implementation(antibytesCatalog.testUtils.coroutine)
                implementation(antibytesCatalog.kfixture)
                implementation(antibytesCatalog.kmock)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(antibytesCatalog.android.google.android.playservice.location)
            }
        }

        val androidUnitTest by getting {
            dependsOn(androidMain)
            dependencies {
                implementation(antibytesCatalog.android.test.junit.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
                implementation(antibytesCatalog.android.test.ktx)
                implementation(antibytesCatalog.jvm.test.mockk)
                implementation(antibytesCatalog.android.test.robolectric)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.test.kotlin.core)
                implementation(antibytesCatalog.jvm.test.junit.junit4)
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

tasks.withType(Test::class.java) {
    testLogging {
        events(FAILED)
    }
}
