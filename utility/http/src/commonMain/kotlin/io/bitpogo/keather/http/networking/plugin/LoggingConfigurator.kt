/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.http.networking.plugin

import io.bitpogo.keather.HttpContract
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

internal class LoggingConfigurator : KtorPluginsContract.LoggingConfigurator {
    override fun configure(pluginConfiguration: Logging.Config, subConfiguration: HttpContract.Logger) {
        pluginConfiguration.logger = subConfiguration
        pluginConfiguration.level = LogLevel.ALL
    }
}
