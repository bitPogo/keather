/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.database

import io.bitpogo.keather.data.weather.KeatherDB

internal const val testDatabase = "test"

expect class DatabaseDriver constructor() {
    val dataBase: KeatherDB
    suspend fun open()
    fun close()
}