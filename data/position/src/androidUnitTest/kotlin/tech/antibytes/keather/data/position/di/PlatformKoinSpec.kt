/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlDriverMock
import com.google.android.gms.location.FusedLocationProviderClient
import tech.antibytes.keather.data.position.PositionQueries
import tech.antibytes.keather.data.position.PositionRepositoryContract
import tech.antibytes.keather.data.position.kmock
import tech.antibytes.keather.interactor.repository.RepositoryContract
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.isNot

@OptIn(KMockExperimental::class)
@KMock(
    SqlDriver::class,
)
@RunWith(RobolectricTestRunner::class)
class PlatformKoinSpec {
    @Test
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
    fun `It contains an FusedLocationProviderClient`() {
        // Given
        val context: Context = mockk(relaxed = true)
        val koin = koinApplication {
            androidContext(context)
            modules(
                resolveLocator(),
            )
        }

        // When
        val client: FusedLocationProviderClient? = koin.koin.getOrNull()

        // Then
        client isNot null
    }

    @Test
    fun `It contains an PositionRepositoryContractLocator`() {
        // Given
        val client: FusedLocationProviderClient = mockk()
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveLocator(),
                module {
                    single { client }
                },
            )
        }

        // When
        val locator: PositionRepositoryContract.Locator? = koin.koin.getOrNull()

        // Then
        locator isNot null
    }

    @Test
    fun `It contains an PositionRepository`() {
        // Given
        val client: FusedLocationProviderClient = mockk()
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolvePositionRepository(),
                module {
                    single { client }
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
