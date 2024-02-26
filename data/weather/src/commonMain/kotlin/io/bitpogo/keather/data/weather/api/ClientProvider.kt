/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.weather.api

import io.bitpogo.keather.data.weather.WeatherRepositoryContract
import io.bitpogo.keather.data.weather.config.MainConfig
import io.bitpogo.keather.http.networking.ClientConfigurator
import io.bitpogo.keather.http.networking.HttpErrorMapper
import io.bitpogo.keather.http.networking.NetworkingContract
import io.bitpogo.keather.http.networking.RequestBuilderFactory
import io.bitpogo.keather.http.networking.plugin.ResponseValidatorConfigurator
import io.bitpogo.keather.http.networking.plugin.SerializerConfigurator
import io.bitpogo.keather.serialization.JsonConfigurator
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
