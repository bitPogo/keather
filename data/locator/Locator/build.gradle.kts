/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
val sdks = listOf("iphoneos", "iphonesimulator")

sdks.forEach { sdk ->
    tasks.create<Exec>("build${sdk.capitalize()}") {
        group = "Build Interop"
        description = "Builds $sdk"

        val libraryName = "Locator"

        commandLine(
            "xcodebuild",
            "-project", "$projectDir/$libraryName.xcodeproj",
            "-scheme", libraryName,
            "-derivedDataPath", "$projectDir/build",
            "-destination", "generic/platform=iOS${ if (sdk == "iphonesimulator") " Simulator" else "" }",
            "-sdk", sdk,
            "-configuration", "Release", "SKIP_INSTALL=NO"
        )

        workingDir(projectDir)

        inputs.files(
            fileTree("$projectDir/$libraryName.xcodeproj") { exclude("**/xcuserdata") },
            fileTree("$projectDir/$libraryName")
        )

        doLast {
            file("$projectDir/build/Build/Products").walkTopDown().forEach { file ->
                if (file.name.endsWith("Locator-Swift.h")) {
                    val content = file.readText()
                        .replace(
                            "@class CLLocationManager;\n@class CLLocation;\n@protocol CLLocationManagerDelegate;",
                            "@class CLLocationManager;\n@class CLLocation;",
                        )
                        .replace(
                            "@class CLLocationManager;\n@class CLLocation;",
                            "@class CLLocationManager;\n@class CLLocation;\n@protocol CLLocationManagerDelegate;"
                        )

                    file.writeText(content)
                }
            }
        }
    }
}

tasks.create<Delete>("clean") {
    group = "build"
    delete("$projectDir/build")
}

tasks.register("buildCryptoLibrary") {
    group = "build"
    description = "Builds all iOS dependencies"

    val dependencies = sdks.map { name -> "build${name.capitalize()}" }
    dependsOn(dependencies)
}
