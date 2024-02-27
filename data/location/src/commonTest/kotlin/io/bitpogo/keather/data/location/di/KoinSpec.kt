/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.location.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlDriverMock
import io.bitpogo.keather.data.location.LocationQueries
import io.bitpogo.keather.data.location.LocationRepositoryContract
import io.bitpogo.keather.data.location.kmock
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
import tech.antibytes.util.test.isNot

@OptIn(KMockExperimental::class)
@KMock(
    SqlDriver::class,
)
class KoinSpec {
    @Test
    @JsName("fn0")
    fun `It contains an LocationRepositoryContractStore`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveLocationRepository(),
                module {
                    single { LocationQueries(kmock<SqlDriverMock>()) }
                },
            )
        }

        // When
        val store: LocationRepositoryContract.Store? = koin.koin.getOrNull()

        // Then
        store isNot null
    }

    @Test
    @JsName("fn1")
    fun `It contains an LocationRepositoryContract`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveLocationRepository(),
                module {
                    single { LocationQueries(kmock<SqlDriverMock>()) }
                    single<CoroutineDispatcher>(named("io")) { StandardTestDispatcher() }
                },
            )
        }

        // When
        val store: RepositoryContract.LocationRepository? = koin.koin.getOrNull()

        // Then
        store isNot null
    }
}
