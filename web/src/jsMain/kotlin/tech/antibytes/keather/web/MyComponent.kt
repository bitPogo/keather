/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:function-naming")
/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.web

import io.kvision.core.Container
import io.kvision.html.div
import io.kvision.html.p
import io.kvision.require

object MyComponent {
    init {
        // require("./sass/MyComponent.scss")
    }

    @Component
    fun Container.MyComponent(
        injectedText: String = "World"
    ) {
        div {
            setAttribute("data-name", "MyComponent")
            p {
                content = "Hello $injectedText indeed!"
            }
        }
    }
}
