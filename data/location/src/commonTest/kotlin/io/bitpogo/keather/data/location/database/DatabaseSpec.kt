/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.location.database

import io.bitpogo.keather.data.location.LocationRepositoryContract
import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.util.test.fulfils

class DatabaseSpec {
    @Test
    @JsName("fnß")
    fun `It fulfils Store`() {
        LocationStore() fulfils LocationRepositoryContract.Store::class
    }
}
