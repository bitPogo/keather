/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.*
import tech.antibytes.gradle.dependency.helper.nodePackage
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)

    alias(antibytesCatalog.plugins.kvision)
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

            compilations.getByName("main") {
                packageJson {
                    customField(
                        "exports", mapOf(
                            "./kotlin/sass/*.scss" to mapOf(
                                 "import" to "./*.scss",
                                 "require" to "./*.scss",
                            )
                        )
                    )
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
                implementation(antibytesCatalog.js.kvision.core)
                implementation(antibytesCatalog.js.kvision.bootstrap.core)
                implementation(antibytesCatalog.js.kvision.chart)

                implementation(antibytesCatalog.common.koin.core)

                nodePackage(antibytesCatalog.node.sass.core)
                nodePackage(antibytesCatalog.node.sass.loader)

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
                implementation(antibytesCatalog.js.test.kvision)
            }
        }
    }
}

val projectPackage = "io.bitpogo.keather.web.app"

kmock {
    rootPackage = projectPackage
}

tasks.create("distributeJsResources", Copy::class) {
    dependsOn("jsBrowserProductionWebpack")
    group = "package"
    val distribution = project.tasks.getByName("jsBrowserProductionWebpack", KotlinWebpack::class).outputDirectory
    val processedResources = project.tasks.getByName("jsProcessResources", Copy::class).destinationDir

    from(distribution) {
        include("*.*")
    }
    from(processedResources)

    destinationDir = file("${layout.buildDirectory.asFile.get()}/app")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    inputs.files(distribution, processedResources)
    outputs.dirs(destinationDir)
}
