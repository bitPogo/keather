/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.web

import io.kvision.Application
import io.kvision.html.p
import io.kvision.panel.root
import io.kvision.require

class KeatherApplication : Application() {
    init {
        require("./sass/style.scss")
    }

    override fun start() {
        root("app") {
            p {
                content = "Hello World"
            }
        }
    }
}
