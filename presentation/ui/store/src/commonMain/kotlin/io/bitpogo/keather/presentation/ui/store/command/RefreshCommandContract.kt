/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.presentation.ui.store.command

interface RefreshCommandContract {
    fun interface Refresh {
        fun refresh()
    }

    fun interface RefreshAll {
        fun refreshAll()
    }
}
