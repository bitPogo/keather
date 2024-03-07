/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.web

import io.kvision.CoreModule
import io.kvision.module
import io.kvision.startApplication

fun main() {
    startApplication(::KeatherApplication, module.hot, CoreModule)
}
