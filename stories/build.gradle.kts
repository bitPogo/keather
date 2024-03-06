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
                        "scripts",
                        mapOf(
                            "storybook" to "start-storybook -p 6006 -c ${layout.projectDirectory.asFile.absolutePath}/.storybook --ci"
                        )
                    )
                }
            }
        }
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(projects.web)
                implementation(antibytesCatalog.node.storybook.builder.webpack5)
                implementation(antibytesCatalog.node.storybook.manager.webpack5)
                implementation(antibytesCatalog.node.storybook.node.logger)
            }
        }
    }
}
