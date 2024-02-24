/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.http.networking.plugin

import io.bitpogo.keather.serialization.JsonConfiguratorContract
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class SerializerConfigurator : KtorPluginsContract.SerializerConfigurator {
    override fun configure(
        pluginConfiguration: ContentNegotiation.Config,
        subConfiguration: JsonConfiguratorContract,
    ) {
        pluginConfiguration.json(
            json = Json { subConfiguration.configure(this) },
        )
    }
}
