/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.web

import io.kvision.core.Container
import io.kvision.html.div
import io.kvision.html.p
import io.kvision.require

object MyComponent {
    init {
        require("./sass/MyComponent.scss")
    }

    @Component
    fun Container.MyComponent() {
        div {
            setAttribute("data-name", "MyComponent")
            p {
                content = "Hello World indeed!"
            }
        }
    }
}
