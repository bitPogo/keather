/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.*
import tech.antibytes.gradle.dependency.helper.nodePackage
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.node)
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
                            "storybook" to "storybook dev -p 6006 -c ${layout.projectDirectory.asFile.absolutePath}/.storybook --ci",
                            "storybook-build" to "storybook build"
                        )
                    )
                    customField(
                        "overrides",
                        mapOf(
                            "react-scripts" to mapOf(
                                "typescript" to "^5"
                            )
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
                implementation(antibytesCatalog.js.kotlinx.wrappers.react.core)
                implementation(antibytesCatalog.js.kotlinx.wrappers.react.dom)
                implementation(antibytesCatalog.js.kvision.core)
                implementation(antibytesCatalog.js.kvision.react)
                nodePackage(antibytesCatalog.node.storybook.builder.webpack5)
                nodePackage(antibytesCatalog.node.storybook.manager.webpack5)
                nodePackage(antibytesCatalog.node.storybook.node.logger)
                nodePackage(antibytesCatalog.node.storybook.react)
                nodePackage(antibytesCatalog.node.storybook.addon.essentials)
                nodePackage(antibytesCatalog.node.storybook.preset.create.react)
                nodePackage(antibytesCatalog.node.react.core)
                nodePackage(antibytesCatalog.node.react.dom)
            }
        }
    }
}


val copyJsStories by tasks.creating(Copy::class.java) {
    dependsOn("assemble")
    from("$projectDir/src/main/js")
    into("${layout.buildDirectory.get().asFile.absolutePath}/compileSync/main/developmentExecutable/kotlin")

    eachFile {
        if (isDirectory) {
            exclude()
        }
        path = path.replace("/", ".")
    }
}

tasks.named<DefaultTask>("build") {
    dependsOn(copyJsStories)
}

val copyPackageJson by tasks.creating(Copy::class.java) {
    dependsOn("build")
    from("$${layout.buildDirectory.get().asFile.absolutePath}/tmp/publicPackageJson/package.json")
    into(projectDir) {
        include("*.*")
    }
}


tasks.register<NpmTask>("start") {
    dependsOn("npmInstall", copyPackageJson)
    mustRunAfter(copyPackageJson)
    args.addAll("run", "storybook")
}
