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
    id("tech.antibytes.gradle.dependency.settings") version "db3eb1b"
}

includeBuild("setup")

dependencyResolutionManagement {
    versionCatalogs {
        getByName("antibytesCatalog") {
            version("minSdk", "21")
            version("kfixture", "0.4.0-SNAPSHOT")
            version("testUtils", "86ec4d6")
            version("kmock", "0.3.0-rc08-SNAPSHOT")
            version("kotlinx-coroutines-core", "1.7.1")
            version("kotlinx-coroutines-test", "1.7.1")
            version("google-android-playservice-location", "21.1.0")

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
                "testUtils-ktor",
                "tech.antibytes.test-utils-kmp",
                "test-utils-ktor",
            ).versionRef("testUtils")
            library(
                "testUtils-resourceloader",
                "tech.antibytes.test-utils-kmp",
                "test-utils-resourceloader",
            ).versionRef("testUtils")
            library(
                "kmock",
                "tech.antibytes.kmock",
                "kmock",
            ).versionRef("kmock")
            plugin("kmock", "tech.antibytes.kmock.kmock-gradle").versionRef("kmock")
            library(
                "android-google-android-playservice-location",
                "com.google.android.gms",
                "play-services-location",
            ).versionRef("google-android-playservice-location")
        }
    }
}

include(
    // ":docs",
)

// Utility
include(
    ":utility:http",
)

// DataLayer
include(
    ":interactor:repository",
    ":data:weather",
    ":data:location",
    ":data:position",
    ":data:position:Locator",
)

// Domain
include(
    ":entity",
)

buildCache {
    fullCache(rootDir)
}

rootProject.name = "keather"
