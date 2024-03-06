/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.web

import io.bitpogo.keather.web.MyComponent.MyComponent
import io.bitpogo.keather.web.di.resolveApp
import io.kvision.Application
import io.kvision.html.p
import io.kvision.panel.root
import io.kvision.require
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeatherApplication : Application() {
    init {
        require("./sass/style.scss")
    }

    private suspend fun render() {
        resolveApp()

        root("app") {
            p {
                content = "Hello World"
            }
            MyComponent()
        }
    }

    override fun start() {
        CoroutineScope(Dispatchers.Main).launch {
            render()
        }
    }
}
