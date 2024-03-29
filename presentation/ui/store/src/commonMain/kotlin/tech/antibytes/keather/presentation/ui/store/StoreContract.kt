/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.presentation.ui.store

import tech.antibytes.keather.presentation.ui.store.command.RefreshCommandsContract.CommandExecutor
import tech.antibytes.keather.presentation.ui.store.command.RefreshCommandsContract.Refresh
import tech.antibytes.keather.presentation.ui.store.command.RefreshCommandsContract.RefreshAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StoreContract {
    interface WeatherStore : Refresh, RefreshAll, CommandExecutor {
        val weatherData: StateFlow<WeatherDataState>
        val error: Flow<WeatherUIError>
    }

    data object WeatherUIError

    sealed interface WeatherDataState
    data object Initial : WeatherDataState
    data object StartUpLoading : WeatherDataState
    data object StartUpError : WeatherDataState

    sealed interface WeatherDataContentfulState : WeatherDataState {
        val data: UIWeatherData
    }

    data class Error(
        override val data: UIWeatherData,
    ) : WeatherDataContentfulState

    data class Loading(
        override val data: UIWeatherData,
    ) : WeatherDataContentfulState

    data class Loaded(
        override val data: UIWeatherData,
    ) : WeatherDataContentfulState

    data class UILocation(
        val name: String,
        val region: String,
        val country: String,
    )

    data class UIRealtimeData(
        val temperature: String,
        val windSpeed: String,
        val precipitation: String,
    )

    data class UIChartData(
        val temperature: Double,
        val precipitation: Double,
    )

    data class UIWeatherData(
        val location: UILocation,
        val realtimeData: UIRealtimeData,
        val historicData: List<UIChartData>,
        val forecast: List<UIChartData>,
    )
}
