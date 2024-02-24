/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.project.config.compability

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion

val Project.javaVersion: JavaLanguageVersion
    get() = JavaLanguageVersion.of(8)

val Project.javaCompatibilityVersion: JavaVersion
    get() = JavaVersion.toVersion(8)
