/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.http.networking.plugin

import io.bitpogo.keather.HttpContract
import io.bitpogo.keather.http.networking.NetworkingContract
import io.bitpogo.keather.serialization.JsonConfiguratorContract
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging

internal interface KtorPluginsContract {
    fun interface ErrorMapper {
        fun mapAndThrow(error: Throwable)
    }

    fun interface LoggingConfigurator : NetworkingContract.PluginConfigurator<Logging.Config, HttpContract.Logger>
    fun interface SerializerConfigurator : NetworkingContract.PluginConfigurator<ContentNegotiation.Config, JsonConfiguratorContract>
    fun interface ResponseValidatorConfigurator : NetworkingContract.PluginConfigurator<HttpCallValidator.Config, ErrorMapper>
}
