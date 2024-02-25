/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position

import io.bitpogo.keather.data.position.KeatherDB

internal const val testDatabase = "test"

expect class DatabaseDriver constructor() {
    val dataBase: KeatherDB
    suspend fun open()
    fun close()
}
