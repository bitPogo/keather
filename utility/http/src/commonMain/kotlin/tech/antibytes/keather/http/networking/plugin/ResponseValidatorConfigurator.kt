/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.http.networking.plugin

import io.ktor.client.plugins.HttpCallValidator

object ResponseValidatorConfigurator : KtorPluginsContract.ResponseValidatorConfigurator {
    override fun configure(
        pluginConfiguration: HttpCallValidator.Config,
        subConfiguration: KtorPluginsContract.ErrorMapper,
    ) {
        pluginConfiguration.handleResponseExceptionWithRequest { error, _ ->
            subConfiguration.mapAndThrow(error)
        }
    }
}
