package io.bitpogo.keather.database

import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import tech.antibytes.util.test.isNot

class DatabaseFactoryTest {
    @Test
    @JsName("fn0")
    fun `Given getInstance is called it creates a Keather Database`() = runTest {
        // When
        val database = DatabaseFactory.getInstance()

        // Then
        database isNot null
    }
}
