/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.http.networking.plugin

import tech.antibytes.keather.http.networking.NetworkingContract
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

object LoggingConfigurator : KtorPluginsContract.LoggingConfigurator {
    override fun configure(pluginConfiguration: Logging.Config, subConfiguration: NetworkingContract.Logger) {
        pluginConfiguration.logger = subConfiguration
        pluginConfiguration.level = LogLevel.ALL
    }
}
