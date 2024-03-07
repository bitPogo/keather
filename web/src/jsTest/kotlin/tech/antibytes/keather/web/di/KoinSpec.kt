/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.web.di

import tech.antibytes.keather.presentation.ui.store.StoreContract
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlinx.coroutines.Dispatchers
import tech.antibytes.util.test.coroutine.runBlockingTestInContext

class KoinSpec {
    // Note the TestScheduler resolves to an error for reasons
    @Test
    @JsName("fn1")
    fun `It contains the WeatherStore`() = runBlockingTestInContext(Dispatchers.Default) {
        // Given
        val koin = resolveApp()

        // When
        val store: StoreContract.WeatherStore? = koin.koin.getOrNull()

        // Then
        assertNotNull(store)
    }
}
