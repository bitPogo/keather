/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.http.networking.plugin

import tech.antibytes.keather.http.networking.NetworkingContract
import tech.antibytes.keather.serialization.JsonConfiguratorContract
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging

interface KtorPluginsContract {
    fun interface ErrorMapper {
        fun mapAndThrow(error: Throwable)
    }

    fun interface LoggingConfigurator : NetworkingContract.PluginConfigurator<Logging.Config, NetworkingContract.Logger>
    fun interface SerializerConfigurator : NetworkingContract.PluginConfigurator<ContentNegotiation.Config, JsonConfiguratorContract>
    fun interface ResponseValidatorConfigurator : NetworkingContract.PluginConfigurator<HttpCallValidator.Config, ErrorMapper>
}
