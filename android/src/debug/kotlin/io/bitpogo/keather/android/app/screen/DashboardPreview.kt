/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bitpogo.keather.presentation.ui.store.StoreContract
import io.bitpogo.keather.presentation.ui.store.command.RefreshCommandsContract
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val data = StoreContract.UIWeatherData(
    location = StoreContract.UILocation(
        name = "Somwhere",
        region = "Nowhere",
        country = "Moonstate of the Moon",
    ),
    realtimeData = StoreContract.UIRealtimeData(
        "24°C",
        "200kmh",
        "2000mm",
    ),
    forecast = listOf(
        StoreContract.UIChartData(
            temperature = 23.0,
            precipitation = 0.0,
        ),
        StoreContract.UIChartData(
            temperature = 26.0,
            precipitation = 0.0,
        ),
        StoreContract.UIChartData(
            temperature = 28.0,
            precipitation = 20.0,
        ),
        StoreContract.UIChartData(
            temperature = 21.0,
            precipitation = 200.0,
        ),
    ),
    historicData = listOf(
        StoreContract.UIChartData(
            temperature = 13.0,
            precipitation = 2002.0,
        ),
        StoreContract.UIChartData(
            temperature = 16.23,
            precipitation = 123.0,
        ),
        StoreContract.UIChartData(
            temperature = 18.0,
            precipitation = 20.0,
        ),
        StoreContract.UIChartData(
            temperature = 20.0,
            precipitation = 10.0,
        ),
    ),
)

class WeatherStoreFake(
    vararg state: StoreContract.WeatherDataState,
) : StoreContract.WeatherStore, RefreshCommandsContract.CommandReceiver, ViewModel() {
    private val states = state.toMutableList()

    private val _weatherData: MutableStateFlow<StoreContract.WeatherDataState> = MutableStateFlow(states.removeFirst())
    override val weatherData: StateFlow<StoreContract.WeatherDataState> = _weatherData
    override val error: Flow<StoreContract.WeatherUIError>
        get() = TODO("Not yet implemented")

    override fun refresh() {
        viewModelScope.launch {
            delay(300)
            val state = states.removeFirstOrNull() ?: _weatherData.value
            _weatherData.update { state }

            if (state == StoreContract.StartUpLoading) {
                refreshAll()
            }
        }
    }

    override fun refreshAll() {
        viewModelScope.launch {
            delay(300)
            val state = states.removeFirstOrNull() ?: _weatherData.value
            _weatherData.update { state }

            if (state == StoreContract.StartUpLoading) {
                refreshAll()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : RefreshCommandsContract.CommandReceiver> runCommand(command: RefreshCommandsContract.Command<T>) = command.execute(this as T)
}

@Preview
@Composable
fun DashboardPreview() {
    Dashboard(
        WeatherStoreFake(
            StoreContract.Initial,
            StoreContract.StartUpLoading,
            StoreContract.StartUpError,
            StoreContract.StartUpLoading,
            StoreContract.Loaded(data),
            StoreContract.Loading(data),
            StoreContract.Error(data),
        ),
    )
}
