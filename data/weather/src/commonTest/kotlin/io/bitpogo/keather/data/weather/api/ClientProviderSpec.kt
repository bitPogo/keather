/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.api

import io.bitpogo.keather.data.weather.config.MainConfig
import io.bitpogo.keather.data.weather.model.remote.Forecast
import io.bitpogo.keather.data.weather.repository.WeatherRepositoryContract
import io.bitpogo.keather.data.weather.resourceLoader
import io.bitpogo.keather.http.networking.receive
import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.http.parameters
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.ktor.Content
import tech.antibytes.util.test.ktor.KtorMockClientFactory.createSimpleMockClient
import tech.antibytes.util.test.mustBe
import kotlin.js.JsName
import kotlin.test.Test

// Note this is only a
class ClientProviderSpec {
    private val serializedForecast = resourceLoader.load("/fixtures/2dayForecast.json")
    private val forecast: Forecast = Json.decodeFromString(serializedForecast)

    @Test
    @JsName("fn1")
    fun `Given provide is called it provides a fresh preconfigured request builder`() = runTest {
        // Given
        val client = createSimpleMockClient(
            Content(
                content = serializedForecast,
                headers = mapOf(
                    "Content-Type" to listOf(
                        ContentType.Application.Json.toString(),
                    ),
                )
            )
        ) {
            url.host mustBe "api.weatherapi.com"
            url.parameters mustBe parameters {
                append("key", MainConfig.apiKey)
            }
        }

        // When
        val requestBuilder = ClientProvider(client).provide()

        // Then
        val response: Forecast = requestBuilder.prepare().receive()
        response mustBe forecast
    }

    @Test
    @JsName("fn0")
    fun `It fulfils ClientProvider`() {
        ClientProvider(HttpClient()) fulfils WeatherRepositoryContract.ClientProvider::class
    }
}
