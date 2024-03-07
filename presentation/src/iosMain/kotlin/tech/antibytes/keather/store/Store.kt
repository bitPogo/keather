/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

internal actual abstract class Store actual constructor() {
    actual val storeScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
}
