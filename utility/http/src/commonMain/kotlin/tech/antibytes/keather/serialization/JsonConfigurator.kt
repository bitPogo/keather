/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.serialization

import kotlinx.serialization.json.JsonBuilder

object JsonConfigurator : JsonConfiguratorContract {
    override fun configure(jsonBuilder: JsonBuilder): JsonBuilder {
        jsonBuilder.isLenient = true
        jsonBuilder.ignoreUnknownKeys = true
        jsonBuilder.allowSpecialFloatingPointValues = true
        jsonBuilder.useArrayPolymorphism = false

        return jsonBuilder
    }
}
