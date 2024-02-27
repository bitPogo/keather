/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlDriverMock
import io.bitpogo.keather.data.position.PositionQueries
import io.bitpogo.keather.data.position.PositionRepositoryContract
import io.bitpogo.keather.data.position.kmock
import io.bitpogo.keather.interactor.repository.RepositoryContract
import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.annotations.IgnoreAndroid
import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.isNot

@OptIn(KMockExperimental::class)
@KMock(
    SqlDriver::class,
)
@RobolectricConfig(manifest = "--none")
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
@IgnoreAndroid
class KoinSpec {
    @Test
    @JsName("fn0")
    fun `It contains an PositionRepositoryContractStore`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePositionRepository(),
                module {
                    single { PositionQueries(kmock<SqlDriverMock>()) }
                },
            )
        }

        // When
        val store: PositionRepositoryContract.Store? = koin.koin.getOrNull()

        // Then
        store isNot null
    }

    @Test
    @JsName("fn1")
    fun `It contains an PositionRepositoryContractLocator`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePositionRepository(),
            )
        }

        // When
        val store: PositionRepositoryContract.Locator? = koin.koin.getOrNull()

        // Then
        store isNot null
    }

    @Test
    @JsName("fn2")
    fun `It contains an PositionRepository`() {
        // Given
        val koin = koinApplication {
            modules(
                resolvePositionRepository(),
                module {
                    single { PositionQueries(kmock<SqlDriverMock>()) }
                    single<CoroutineDispatcher>(named("io")) { StandardTestDispatcher() }
                },
            )
        }

        // When
        val store: RepositoryContract.PositionRepository? = koin.koin.getOrNull()

        // Then
        store isNot null
    }
}
