/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.database

internal const val testDatabase = "test"

expect class DatabaseDriver constructor() {
    val dataBase: KeatherDB
    suspend fun open()
    fun close()
}
