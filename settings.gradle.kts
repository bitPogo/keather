/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.settings.fullCache

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
    id("tech.antibytes.gradle.dependency.settings") version "283c93a"
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
            version("vico", "1.14.0")
            version("node-react-test", "14.2.1")
            version("material3", "1.2.0")
            version("kvision", "7.4.2")
            version("sass", "^1.29.0")
            version("sass-loader", "^10.1.0")
            version("gradle-node", "7.0.2")
            version("storybook", "^7.6.17")

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
            library(
                "vico-compose-core",
                "com.patrykandpatrick.vico",
                "compose",
            ).versionRef("vico")
            library(
                "vico-compose-m2",
                "com.patrykandpatrick.vico",
                "compose-m2",
            ).versionRef("vico")
            library(
                "vico-compose-m3",
                "com.patrykandpatrick.vico",
                "compose-m3",
            ).versionRef("vico")
            library(
                "vico-core",
                "com.patrykandpatrick.vico",
                "core",
            ).versionRef("vico")
            library(
                "vico-views",
                "com.patrykandpatrick.vico",
                "views",
            ).versionRef("vico")
            library(
                "compose-material3",
                "androidx.compose.material3",
                "material3",
            ).versionRef("material3")
            library(
                "js-kvision-core",
                "io.kvision",
                "kvision",
            ).versionRef("kvision")
            library(
                "js-kvision-bootstrap-core",
                "io.kvision",
                "kvision-bootstrap",
            ).versionRef("kvision")
            library(
                "js-kvision-chart",
                "io.kvision",
                "kvision-chart",
            ).versionRef("kvision")
            library(
                "js-test-kvision",
                "io.kvision",
                "kvision-testutils",
            ).versionRef("kvision")
            plugin("kvision", "io.kvision").versionRef("kvision")
            library(
                "node-sass-core",
                "node-production",
                "sass",
            ).versionRef("sass")
            library(
                "node-sass-loader",
                "node-development",
                "sass-loader",
            ).versionRef("sass-loader")
            plugin(
                "gradle-node",
                "com.github.node-gradle.node",
            ).versionRef("gradle-node")
            library(
                "node-storybook-builder-webpack5",
                "node-production",
                "@storybook/builder-webpack5",
            ).versionRef("storybook")
            library(
                "node-storybook-manager-webpack5",
                "node-production",
                "@storybook/manager-webpack5",
            ).versionRef("storybook")
            library(
                "node-storybook-node-logger",
                "node-production",
                "@storybook/node-logger",
            ).versionRef("storybook")
        }
    }
}

include(
    ":docs",
)

// Utility
include(
    ":utility:http",
    ":utility:koin",
)

// DataLayer
include(
    ":data:weather",
    ":data:location",
    ":data:position",
    ":data:position:Locator",
    ":database",
)

// Domain
include(
    ":entity",
    ":domain",
    ":domain:repository",
)

// Presentation
include(
    ":presentation",
    ":presentation:interactor",
    ":presentation:ui:store",
)

// Platform
// Android
include(":android")

// Js - Browser/Web
include(
    ":web",
    ":stories",
)

buildCache {
    fullCache(rootDir)
}

rootProject.name = "keather"
