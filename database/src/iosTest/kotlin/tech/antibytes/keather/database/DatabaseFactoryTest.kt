/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.database

import kotlin.test.Test
import tech.antibytes.util.test.isNot

class DatabaseFactoryTest {
    @Test
    fun `Given getInstance is called it creates a Keather Database`() {
        // When
        val database = DatabaseFactory.getInstance()

        // Then
        database isNot null
    }
}
