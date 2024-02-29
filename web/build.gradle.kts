/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.*
import tech.antibytes.gradle.dependency.helper.nodePackage

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)

    alias(antibytesCatalog.plugins.kmock)
}

kotlin {
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

        binaries.executable()
        browser {
            commonWebpackConfig {
                mode = if (project.hasProperty("prod")) {
                    Mode.PRODUCTION
                } else {
                    Mode.DEVELOPMENT
                }
            }
        }
        useCommonJs()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.coroutines.core)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)
                implementation(antibytesCatalog.js.kotlin.wrappers.browser)

                implementation(antibytesCatalog.common.koin.core)

                implementation(projects.presentation)
                implementation(projects.presentation.ui.store)
                implementation(projects.domain)
                implementation(projects.data.position)
                implementation(projects.data.location)
                implementation(projects.data.weather)
                implementation(projects.database)
                implementation(projects.entity)
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(antibytesCatalog.js.test.kotlin.core)
                implementation(antibytesCatalog.testUtils.core)
                implementation(antibytesCatalog.testUtils.coroutine)
                implementation(antibytesCatalog.kfixture)
                implementation(antibytesCatalog.kmock)
            }
        }
    }
}

val projectPackage = "io.bitpogo.keather.web.app"

kmock {
    rootPackage = projectPackage
}
