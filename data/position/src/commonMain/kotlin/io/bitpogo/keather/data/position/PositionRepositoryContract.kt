/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position

import io.bitpogo.keather.data.position.model.store.SaveablePosition
import io.bitpogo.keather.entity.Position

internal interface PositionRepositoryContract {
    fun interface Locator {
        suspend fun fetchPosition(): Result<Position>
    }

    interface Store {
        suspend fun savePosition(position: SaveablePosition)
        suspend fun fetchPosition(): Result<SaveablePosition>
    }
}
