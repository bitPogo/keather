package io.bitpogo.keather.database

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
