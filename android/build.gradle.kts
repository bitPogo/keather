/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.configuration.runtime.AntiBytesTestConfigurationTask

plugins {
    id(antibytesCatalog.plugins.kotlin.android.get().pluginId)

    alias(antibytesCatalog.plugins.gradle.antibytes.androidApplicationConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    alias(antibytesCatalog.plugins.test.roborazzi.gradle)

    alias(antibytesCatalog.plugins.kmock)
}

val projectPackage = "io.bitpogo.keather.android.app"

kmock {
    rootPackage = projectPackage
}

android {
    namespace = projectPackage

    defaultConfig {
        applicationId = projectPackage
        versionCode = 1
        versionName = "1.0"
        minSdk = 24

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
        viewBinding = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = antibytesCatalog.versions.android.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/dversions/**"
            excludes += "/META-INF/**"
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            isMinifyEnabled = false
            matchingFallbacks.add("release")
        }
    }

    sourceSets {
        getByName("test") {
            java.srcDirs(layout.buildDirectory.dir("generated/antibytes/test/kotlin"))
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                // -Pscreenshot to filter screenshot tests
                it.useJUnit {
                    if (project.hasProperty("screenshot")) {
                        includeCategories("io.github.takahirom.roborazzi.testing.category.ScreenshotTests")
                    }
                }
            }
        }
    }
}

roborazzi {
    outputDir.set(project.layout.projectDirectory.dir("src/test/snapshots/roborazzi/images"))
}

dependencies {
    implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk8)

    implementation(antibytesCatalog.android.ktx.core)
    implementation(antibytesCatalog.android.ktx.viewmodel.core)
    implementation(antibytesCatalog.android.ktx.viewmodel.compose)

    implementation(antibytesCatalog.android.appCompact.core)

    implementation(antibytesCatalog.android.ktx.activity.compose)
    implementation(antibytesCatalog.android.compose.ui.core)
    implementation(antibytesCatalog.android.material.compose.core)

    implementation(antibytesCatalog.android.compose.foundation.core)

    implementation(antibytesCatalog.android.material.core)
    implementation(antibytesCatalog.android.material.compose.icons)
    implementation(antibytesCatalog.android.material.compose.extendedIcons)

    implementation(antibytesCatalog.common.ktor.client.logging)

    implementation(antibytesCatalog.vico.compose.core)
    implementation(antibytesCatalog.vico.compose.m3)
    implementation(antibytesCatalog.vico.core)

    implementation(antibytesCatalog.common.koin.core)

    implementation(projects.presentation)
    implementation(projects.presentation.ui.store)
    implementation(projects.domain)
    implementation(projects.data.position)
    implementation(projects.data.location)
    implementation(projects.data.weather)
    implementation(projects.database)
    implementation(projects.entity)
    implementation(projects.utility.koin)
    implementation(antibytesCatalog.android.ktx.navigation.compose)

    testImplementation(antibytesCatalog.testUtils.core)
    testImplementation(antibytesCatalog.testUtils.coroutine)
    testImplementation(antibytesCatalog.kfixture)
    testImplementation(antibytesCatalog.kmock)
    testImplementation(antibytesCatalog.android.test.junit.core)
    testImplementation(antibytesCatalog.jvm.test.junit.junit4)
    testImplementation(antibytesCatalog.android.test.compose.core)
    testImplementation(antibytesCatalog.android.test.compose.junit4Rule)
    testImplementation(antibytesCatalog.jvm.test.koin)

    testImplementation(antibytesCatalog.android.test.roborazzi.compose)
    testImplementation(antibytesCatalog.android.test.roborazzi.junit)
    testImplementation(antibytesCatalog.android.test.roborazzi.core)
    testImplementation(antibytesCatalog.android.test.robolectric)
    testImplementation(antibytesCatalog.android.test.espresso.core)
    testImplementation(antibytesCatalog.android.test.koin.androidTest)

    // Debug
    debugImplementation(antibytesCatalog.android.compose.ui.tooling.core)
    debugImplementation(antibytesCatalog.android.test.compose.manifest)

    androidTestImplementation(antibytesCatalog.android.test.junit.core)
    androidTestImplementation(antibytesCatalog.jvm.test.junit.junit4)
    androidTestImplementation(antibytesCatalog.android.test.compose.junit4)
    androidTestImplementation(antibytesCatalog.android.test.espresso.core)
    androidTestImplementation(antibytesCatalog.android.test.uiAutomator)


    androidTestImplementation(antibytesCatalog.testUtils.core)
    androidTestImplementation(antibytesCatalog.kfixture)
    androidTestImplementation(antibytesCatalog.kmock)
}

val provideTestConfig by tasks.creating(AntiBytesTestConfigurationTask::class.java) {
    mustRunAfter("clean")
    packageName.set("$projectPackage.config")
    stringFields.set(
        mapOf(
            "snapshotDir" to project.layout.projectDirectory.dir("src/test/snapshots/roborazzi/images").asFile.absolutePath
        )
    )

    mustRunAfter("clean")
}

tasks.withType(KotlinCompile::class.java) {
    dependsOn(provideTestConfig)
    mustRunAfter(provideTestConfig)
}
