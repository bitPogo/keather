import tech.antibytes.gradle.dependency.settings.fullCache

/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        val antibytesPlugins = "^tech\\.antibytes\\.[\\.a-z\\-]+"
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
    }
}

plugins {
    id("tech.antibytes.gradle.dependency.settings") version "288f8da"
}

includeBuild("setup")

dependencyResolutionManagement {
    versionCatalogs {
        getByName("antibytesCatalog") {
            version("minSdk", "21")
            version("kfixture", "0.4.0-SNAPSHOT")
            version("testUtils", "b6c6e6c")
            version("kmock", "0.3.0-rc08-SNAPSHOT")
            version("kotlinx-coroutines-core", "1.7.1")
            version("kotlinx-coroutines-test", "1.7.1")

            library("kfixture", "tech.antibytes.kfixture", "core").versionRef("kfixture")
            library("testUtils-core", "tech.antibytes.test-utils-kmp", "test-utils").versionRef("testUtils")
            library(
                "testUtils-annotations",
                "tech.antibytes.test-utils-kmp",
                "test-utils-annotations-junit4",
            ).versionRef("testUtils")
            library(
                "testUtils-coroutine",
                "tech.antibytes.test-utils-kmp",
                "test-utils-coroutine",
            ).versionRef("testUtils")
            library(
                "testUtils-coroutine",
                "tech.antibytes.test-utils-kmp",
                "test-utils-coroutine",
            ).versionRef("testUtils")
            library(
                "kmock",
                "tech.antibytes.kmock",
                "kmock",
            ).versionRef("kmock")
            plugin("kmock", "tech.antibytes.kmock.kmock-gradle").versionRef("kmock")
        }
    }
}

include(
    ":kmp-lib",
    ":example-android-application",
    // ":docs",
)

buildCache {
    fullCache(rootDir)
}

rootProject.name = "template-project"
