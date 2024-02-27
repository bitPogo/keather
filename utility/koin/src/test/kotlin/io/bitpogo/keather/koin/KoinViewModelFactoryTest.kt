/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.koin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class KoinViewModelFactoryTest {
    @Test
    fun `Given create is called it fails if the given type is not bounded to a ViewModel`() {
        // Given
        val koin = koinApplication {
            modules(
                module {
                    scope<Any> {
                        factory<Type> {
                            InvalidViewModel
                        }
                    }
                },
            )
        }
        val extras: CreationExtras = mockk()

        val scope = koin.koin.createScope<Any>()

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            KoinViewModelFactory.getInstance(Type::class, scope).create(ViewModel::class.java, extras)
        }

        assertEquals(
            actual = error.message,
            expected = "The given Type is not bounded to a ViewModel!",
        )
    }

    @Test
    fun `Given create is called it returns a ViewModel if the given type is not bounded to a ViewModel`() {
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
        val extras: CreationExtras = mockk()

        val scope = koin.koin.createScope<Any>()

        // When
        val viewModel = KoinViewModelFactory
            .getInstance(Type::class, scope)
            .create(ViewModel::class.java, extras)

        assertTrue { viewModel is Type }
    }

    @Test
    fun `Given create is called it returns a ViewModel with a Qualifier if the given type is not bounded to a ViewModel`() {
        // Given
        val qualifier = "test"
        val koin = koinApplication {
            modules(
                module {
                    scope<Any> {
                        factory<Type>(qualifier = named(qualifier)) {
                            ValidViewModel
                        }
                    }
                },
            )
        }
        val extras: CreationExtras = mockk()

        val scope = koin.koin.createScope<Any>()

        // When
        val viewModel = KoinViewModelFactory
            .getInstance(
                Type::class,
                scope,
                qualifier = named(qualifier),
            )
            .create(ViewModel::class.java, extras)

        assertTrue { viewModel is Type }
    }

    @Test
    fun `Given create is called it returns a ViewModel with Parameter if the given type is not bounded to a ViewModel`() {
        // Given
        val givenParameter = parametersOf("test")
        val koin = koinApplication {
            modules(
                module {
                    scope<Any> {
                        factory<Type> { capturedParameter ->
                            assertEquals(
                                actual = capturedParameter.values,
                                expected = givenParameter.values,
                            )
                            ValidViewModel
                        }
                    }
                },
            )
        }
        val extras: CreationExtras = mockk()

        val scope = koin.koin.createScope<Any>()

        // When
        val viewModel = KoinViewModelFactory
            .getInstance(
                Type::class,
                scope,
                params = { givenParameter },
            )
            .create(ViewModel::class.java, extras)

        assertTrue { viewModel is Type }
    }

    private interface Type
    private object InvalidViewModel : Type
    private object ValidViewModel : Type, ViewModel()
}
