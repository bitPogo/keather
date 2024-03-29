/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather.model.api

import kotlinx.serialization.Serializable

@Serializable
internal data class Condition(
    val text: String,
    val icon: String,
    val code: Int,
)
