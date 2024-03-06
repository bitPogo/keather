/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.web

import io.kvision.panel.Root
import kotlin.test.BeforeTest

abstract class KVisionTest {
    lateinit var root: Root

    @BeforeTest
    fun setUp() {
        root = Root("testRoot")
    }
}
