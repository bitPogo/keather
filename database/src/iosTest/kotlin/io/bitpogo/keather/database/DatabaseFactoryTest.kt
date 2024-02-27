package io.bitpogo.keather.database

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
