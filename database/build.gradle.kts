import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import tech.antibytes.gradle.configuration.sourcesets.iosx
import tech.antibytes.gradle.dependency.helper.nodeDevelopmentPackage
import tech.antibytes.gradle.dependency.helper.nodeProductionPackage
import tech.antibytes.gradle.project.config.database.SqlDelight

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)

    id(antibytesCatalog.plugins.square.sqldelight.get().pluginId)
    alias(antibytesCatalog.plugins.kmock)
}

val projectPackage = "io.bitpogo.keather.database"


val featureTables = listOf(
    projects.data.position,
    projects.data.location,
    projects.data.weather,
)

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

    iosx()
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
                implementation(antibytesCatalog.common.koin.core)
                implementation(antibytesCatalog.common.square.sqldelight.primitiveAdapters)

                featureTables.forEach { feature ->
                    implementation(feature)
                }
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
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(antibytesCatalog.android.square.sqldelight.driver)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(antibytesCatalog.android.koin.androidBinding)
                implementation(antibytesCatalog.android.test.junit.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
                implementation(antibytesCatalog.android.test.ktx)
                implementation(antibytesCatalog.jvm.test.mockk)
                implementation(antibytesCatalog.android.test.robolectric)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.square.sqldelight.driver.native)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)
                implementation(antibytesCatalog.js.kotlin.wrappers.browser)
                implementation(antibytesCatalog.js.square.sqldelight.driver)
                nodeProductionPackage(antibytesCatalog.node.sqlJs)
                nodeDevelopmentPackage(antibytesCatalog.node.copyWebpackPlugin)
                nodeProductionPackage(antibytesCatalog.node.sqlJsWorker)
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

sqldelight {
    databases {
        create(SqlDelight.databaseName) {
            packageName.set(projectPackage)
            srcDirs.setFrom("src/commonMain/database")
            generateAsync = true
            schemaOutputDirectory.set(
                layout.projectDirectory.dir("src/commonMain/database/schema")
            )
            verifyMigrations = true
            dependencies {
                featureTables.forEach { feature ->
                    dependency(feature.dependencyProject)
                }
            }
        }
    }
}
