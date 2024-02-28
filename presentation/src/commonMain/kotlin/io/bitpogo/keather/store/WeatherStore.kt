/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.store

import io.bitpogo.keather.entity.LocalizedWeatherData
import io.bitpogo.keather.presentation.interactor.InteractorContract
import io.bitpogo.keather.presentation.ui.store.StoreContract
import io.bitpogo.keather.presentation.ui.store.command.RefreshCommandsContract
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

    private fun Result<LocalizedWeatherData>.toLoaded(): StoreContract.WeatherDataState {
        return StoreContract.Loaded(getOrThrow())
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
