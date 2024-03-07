/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.http.networking.plugin

import tech.antibytes.keather.serialization.JsonConfiguratorContract
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object SerializerConfigurator : KtorPluginsContract.SerializerConfigurator {
    override fun configure(
        pluginConfiguration: ContentNegotiation.Config,
        subConfiguration: JsonConfiguratorContract,
    ) {
        pluginConfiguration.json(
            json = Json { subConfiguration.configure(this) },
        )
    }
}
