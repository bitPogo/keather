/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.location

import io.bitpogo.keather.data.position.model.store.SaveablePosition

internal interface LocationRepositoryContract {
    fun interface Store {
        suspend fun fetchLocation(): Result<SaveablePosition>
    }
}
