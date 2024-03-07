/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.store

import kotlinx.coroutines.CoroutineScope

internal expect abstract class Store constructor() {
    protected val storeScope: CoroutineScope
}
