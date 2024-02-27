/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.api

import io.bitpogo.keather.data.weather.ClientProviderMock
import io.bitpogo.keather.data.weather.WeatherRepositoryContract
import io.bitpogo.keather.data.weather.kmock
import io.bitpogo.keather.data.weather.model.api.Forecast
import io.bitpogo.keather.data.weather.model.api.History
import io.bitpogo.keather.data.weather.model.api.RequestPosition
import io.bitpogo.keather.data.weather.resourceLoader
import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.http.networking.FakeHttpCall
import io.bitpogo.keather.http.networking.NetworkingContract
import io.bitpogo.keather.http.networking.NetworkingContract.RequestBuilder
import io.bitpogo.keather.http.networking.RequestBuilderMock
import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.ClockMock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.assertProxy
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs

@OptIn(KMockExperimental::class)
@KMock(
    RequestBuilder::class,
    WeatherRepositoryContract.ClientProvider::class,
)
class WeatherApiSpec {
    private val fixture = kotlinFixture()
    private val requestLocation = RequestPosition(
        Longitude(fixture.fixture()),
        Latitude(fixture.fixture()),
    )
    private val requestBuilder: RequestBuilderMock = kmock()
    private val clientProvider: ClientProviderMock = kmock()
    private val forecast: Forecast = Json.decodeFromString(resourceLoader.load("/fixtures/2dayForecast.json"))
    private val history: History = Json.decodeFromString(resourceLoader.load("/fixtures/2dayHistory.json"))
    private val clock: ClockMock = kmock()
    private val now = 1708953253L

    @BeforeTest
    fun setUp() {
        clientProvider._provide returns requestBuilder

        requestBuilder._clearMock()

        requestBuilder._setBody returns requestBuilder
        requestBuilder._setHeaders returns requestBuilder
        requestBuilder._addParameter returns requestBuilder

        clock._now returns Instant.fromEpochSeconds(now)
    }

    @Test
    @JsName("fn1a")
    fun `Given fetchForecast it propagates Errors`() = runTest {
        // Given
        requestBuilder._prepare returns FakeHttpCall { history }

        // When
        val actual = WeatherApi(clock, clientProvider).fetchForecast(requestLocation)

        // Then
        actual.isFailure mustBe true
    }

    @Test
    @JsName("fn1")
    fun `Given fetchForecast it calls the WeatherApi`() = runTest {
        // Given
        requestBuilder._prepare returns FakeHttpCall { forecast }

        // When
        val actual = WeatherApi(clock, clientProvider).fetchForecast(requestLocation)

        // Then
        actual.getOrNull() sameAs forecast
        assertProxy {
            requestBuilder._addParameter.hasBeenStrictlyCalledWith(
                mapOf(
                    "q" to "${requestLocation.latitude.lat},${requestLocation.longitude.long}",
                    "unixdt_end" to 1709558053L,
                    "days" to 7,
                ),
            )
            requestBuilder._prepare.hasBeenStrictlyCalledWith(
                NetworkingContract.Method.GET,
                listOf("v1", "forecast.json"),
            )
        }
    }

    @Test
    @JsName("fn2")
    fun `Given fetchHistory it calls the WeatherApi`() = runTest {
        // Given
        requestBuilder._prepare returns FakeHttpCall { history }

        // When
        val actual = WeatherApi(clock, clientProvider).fetchHistory(requestLocation)

        // Then
        actual.getOrNull() sameAs history
        assertProxy {
            requestBuilder._addParameter.hasBeenStrictlyCalledWith(
                mapOf(
                    "q" to "${requestLocation.latitude.lat},${requestLocation.longitude.long}",
                    "unixdt" to 1707743653L,
                    "unixend_dt" to now,
                ),
            )
            requestBuilder._prepare.hasBeenStrictlyCalledWith(
                NetworkingContract.Method.GET,
                listOf("v1", "history.json"),
            )
        }
    }

    @Test
    @JsName("fn2a")
    fun `Given fetchHistory it propagates Errors`() = runTest {
        // Given
        requestBuilder._prepare returns FakeHttpCall { forecast }

        // When
        val actual = WeatherApi(clock, clientProvider).fetchHistory(requestLocation)

        // Then
        actual.isFailure mustBe true
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Remote`() {
        WeatherApi(clock, clientProvider) fulfils WeatherRepositoryContract.Api::class
    }
}
