/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.store

import tech.antibytes.keather.entity.Forecast
import tech.antibytes.keather.entity.HistoricData
import tech.antibytes.keather.entity.LocalizedWeatherData
import tech.antibytes.keather.entity.Location
import tech.antibytes.keather.entity.RealtimeData
import tech.antibytes.keather.presentation.interactor.InteractorContract
import tech.antibytes.keather.presentation.ui.store.StoreContract
import tech.antibytes.keather.presentation.ui.store.command.RefreshCommandsContract
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class WeatherStore(
    private val interactor: InteractorContract.WeatherDataInteractor,
) : StoreContract.WeatherStore, Store() {
    private val _weatherData: MutableStateFlow<StoreContract.WeatherDataState> = MutableStateFlow(StoreContract.Initial)
    override val weatherData: StateFlow<StoreContract.WeatherDataState> = _weatherData

    private val _error = Channel<StoreContract.WeatherUIError>()
    override val error: Flow<StoreContract.WeatherUIError> = _error.receiveAsFlow()

    init {
        initializeWeatherStore()
    }

    private fun propagateError() {
        storeScope.launch {
            _error.send(StoreContract.WeatherUIError)
        }
    }

    private fun toErrorState(): StoreContract.WeatherDataState {
        propagateError()

        return if (_weatherData.value is StoreContract.WeatherDataContentfulState) {
            StoreContract.Error((_weatherData.value as StoreContract.WeatherDataContentfulState).data)
        } else {
            StoreContract.StartUpError
        }
    }

    private fun Location.toUILocation(): StoreContract.UILocation {
        return StoreContract.UILocation(
            name = name.name,
            region = region.region,
            country = country.country,
        )
    }

    // Formatter can do wounders here
    private fun RealtimeData.toUIRealtimeData(): StoreContract.UIRealtimeData {
        return StoreContract.UIRealtimeData(
            temperature = "${temperatureInCelsius.temperature}°C",
            windSpeed = "${windSpeedInKilometerPerHour.speed}kmh",
            precipitation = "${precipitationInMillimeter.precipitation}mm",
        )
    }

    private fun foreCastToUIChart(forecast: Forecast): StoreContract.UIChartData {
        return StoreContract.UIChartData(
            temperature = forecast.averageTemperatureInCelsius.temperature,
            precipitation = forecast.precipitationInMillimeter.precipitation,
        )
    }

    private fun historicDataToUIChart(data: HistoricData): StoreContract.UIChartData {
        return StoreContract.UIChartData(
            temperature = data.averageTemperatureInCelsius.temperature,
            precipitation = data.precipitationInMillimeter.precipitation,
        )
    }

    private fun LocalizedWeatherData.toUIWeatherData(): StoreContract.UIWeatherData {
        return StoreContract.UIWeatherData(
            location = location.toUILocation(),
            realtimeData = realtimeData.toUIRealtimeData(),
            forecast = forecast.map(::foreCastToUIChart),
            historicData = history.map(::historicDataToUIChart),
        )
    }

    private fun Result<LocalizedWeatherData>.toLoaded(): StoreContract.WeatherDataState {
        return StoreContract.Loaded(getOrThrow().toUIWeatherData())
    }

    private fun Result<LocalizedWeatherData>.toUIState(): StoreContract.WeatherDataState {
        return if (isFailure) {
            toErrorState()
        } else {
            toLoaded()
        }
    }

    private fun update(weatherData: Result<LocalizedWeatherData>) {
        _weatherData.update { weatherData.toUIState() }
    }

    private fun initializeWeatherStore() {
        interactor.weatherData
            .onEach(::update)
            .launchIn(storeScope)
    }

    private fun goIntoLoadingState() {
        val state = if (_weatherData.value is StoreContract.WeatherDataContentfulState) {
            StoreContract.Loading((_weatherData.value as StoreContract.WeatherDataContentfulState).data)
        } else {
            StoreContract.StartUpLoading
        }

        _weatherData.update { state }
    }

    override fun refresh() {
        storeScope.launch {
            goIntoLoadingState()
            interactor.refresh(this)
        }
    }

    override fun refreshAll() {
        storeScope.launch {
            goIntoLoadingState()
            interactor.refreshAll(this)
        }
    }

    override fun <T : RefreshCommandsContract.CommandReceiver> runCommand(
        command: RefreshCommandsContract.Command<T>,
    ) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
