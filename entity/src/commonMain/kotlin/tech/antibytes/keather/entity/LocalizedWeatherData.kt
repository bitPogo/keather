/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.entity

data class LocalizedWeatherData(
    val location: Location,
    val realtimeData: RealtimeData,
    val forecast: List<Forecast>,
    val history: List<HistoricData>,
)
