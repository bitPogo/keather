/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.koin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class ViewModelResolverTest {
    @BeforeEach
    fun setup() {
        mockkObject(KoinViewModelFactory.Companion)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(KoinViewModelFactory.Companion)
    }

    @Test
    fun `Given resolveViewModelAdv is called it fails if the given Type is only anonymous or local`() {
        // Given
        val koin = koinApplication {
            modules(
                module {
                    scope<Any> {
                        factory<Type> {
                            ValidViewModel
                        }
                    }
                },
            )
        }

        val local = object {}

        val scope = koin.koin.createScope<Any>()

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            resolveViewModelAdv(
                local::class,
                mockk(),
                mockk(),
                null,
                scope,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Local and anonymous classes can not be ViewModels!",
        )
    }

    @Test
    fun `Given resolveViewModelAdv is creates a ViewModel`() {
        // Given
        val koin = koinApplication {
            modules(
                module {
                    scope<Any> {
                        factory<Type> {
                            ValidViewModel
                        }
                    }
                },
            )
        }
        val extras: CreationExtras = mockk(relaxed = true)
        val store = ViewModelStore()
        val scope = koin.koin.createScope<Any>()

        // When
        val viewModel = resolveViewModelAdv(
            Type::class,
            store,
            extras,
            null,
            scope,
        ) as Any

        // Then
        assertTrue { viewModel is Type }
        verify(exactly = 1) {
            KoinViewModelFactory.Companion.getInstance(
                Type::class,
                scope,
                null,
                null,
            )
        }
    }

    @Test
    fun `Given resolveViewModelAdv is creates a ViewModel with Qualifier`() {
        // Given
        val qualifier = named("something")
        val koin = koinApplication {
            modules(
                module {
                    scope<Any> {
                        factory<Type>(qualifier = qualifier) {
                            ValidViewModel
                        }
                    }
                },
            )
        }
        val extras: CreationExtras = mockk(relaxed = true)
        val store = ViewModelStore()
        val scope = koin.koin.createScope<Any>()

        // When
        val viewModel = resolveViewModelAdv(
            Type::class,
            store,
            extras,
            qualifier,
            scope,
        ) as Any

        // Then
        assertTrue { viewModel is Type }
        verify(exactly = 1) {
            KoinViewModelFactory.Companion.getInstance(
                Type::class,
                scope,
                qualifier,
                null,
            )
        }
    }

    @Test
    fun `Given resolveViewModelAdv is creates a ViewModel parameter`() {
        // Given
        val parameter = { parametersOf("test") }
        val koin = koinApplication {
            modules(
                module {
                    scope<Any> {
                        factory<Type> {
                            ValidViewModel
                        }
                    }
                },
            )
        }
        val extras: CreationExtras = mockk(relaxed = true)
        val store = ViewModelStore()
        val scope = koin.koin.createScope<Any>()

        // When
        val viewModel = resolveViewModelAdv(
            Type::class,
            store,
            extras,
            null,
            scope,
            parameter,
        ) as Any

        // Then
        assertTrue { viewModel is Type }
        verify(exactly = 1) {
            KoinViewModelFactory.Companion.getInstance(
                Type::class,
                scope,
                null,
                parameter,
            )
        }
    }

    private interface Type
    private object ValidViewModel : Type, ViewModel()
}
