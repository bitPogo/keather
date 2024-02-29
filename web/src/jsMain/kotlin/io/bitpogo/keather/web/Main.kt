/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.web

import kotlinx.browser.document

fun main() {
    document.getElementById("app")?.apply {
        this.innerHTML = "<p>Hello World</p>"
    }
}
