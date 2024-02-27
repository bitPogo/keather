/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.presentation.ui.store

import io.bitpogo.keather.entity.LocalizedWeatherData
import io.bitpogo.keather.presentation.ui.store.command.RefreshCommandContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StoreContract {
    interface WeatherStore : RefreshCommandContract.Refresh, RefreshCommandContract.RefreshAll {
        val weatherData: StateFlow<WeatherDataState>
        val error: Flow<WeatherUIError>
    }

    data object WeatherUIError

    sealed interface WeatherDataState
    data object Initial : WeatherDataState
    data object StartUpLoading : WeatherDataState

    sealed interface WeatherDataContentfulState : WeatherDataState {
        val data: LocalizedWeatherData
    }

    data class Loading(
        override val data: LocalizedWeatherData,
    ) : WeatherDataContentfulState

    data class Loaded(
        override val data: LocalizedWeatherData,
    ) : WeatherDataContentfulState
}