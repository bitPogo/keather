/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.database

import android.content.Context
import io.mockk.mockk
import org.junit.Test
import tech.antibytes.util.test.isNot

class DatabaseFactoryTest {
    @Test
    fun `Given getInstance is called it creates a Keather Database`() {
        // Given
        val context: Context = mockk(relaxed = true)

        // When
        val database = DatabaseFactory.getInstance(context)

        // Then
        database isNot null
    }
}
