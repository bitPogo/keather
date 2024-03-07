/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather.api

import tech.antibytes.keather.data.weather.WeatherRepositoryContract
import tech.antibytes.keather.data.weather.config.MainConfig
import tech.antibytes.keather.http.networking.ClientConfigurator
import tech.antibytes.keather.http.networking.HttpErrorMapper
import tech.antibytes.keather.http.networking.NetworkingContract
import tech.antibytes.keather.http.networking.RequestBuilderFactory
import tech.antibytes.keather.http.networking.plugin.ResponseValidatorConfigurator
import tech.antibytes.keather.http.networking.plugin.SerializerConfigurator
import tech.antibytes.keather.serialization.JsonConfigurator
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

internal class ClientProvider(
    private val client: HttpClient = HttpClient(),
) : WeatherRepositoryContract.ClientProvider {
    private fun initPlugins(): Set<NetworkingContract.Plugin<in Any, in Any?>> {
        val jsonConfig = JsonConfigurator
        Json { jsonConfig.configure(this) }

        @Suppress("UNCHECKED_CAST")
        return setOf(
            NetworkingContract.Plugin(
                ContentNegotiation,
                SerializerConfigurator,
                jsonConfig,
            ) as NetworkingContract.Plugin<in Any, in Any?>,
            NetworkingContract.Plugin(
                HttpCallValidator,
                ResponseValidatorConfigurator,
                HttpErrorMapper,
            ) as NetworkingContract.Plugin<in Any, in Any?>,
        )
    }

    override fun provide(): NetworkingContract.RequestBuilder {
        val client = client.config {
            expectSuccess = false
            ClientConfigurator.configure(
                this,
                initPlugins(),
            )
        }

        return RequestBuilderFactory(
            client = client,
            host = "api.weatherapi.com",
        ).create().setParameter(
            mapOf(
                "key" to MainConfig.apiKey,
            ),
        )
    }
}
