/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.android.app.screen

import tech.antibytes.keather.presentation.ui.store.StoreContract
import tech.antibytes.keather.presentation.ui.store.command.RefreshCommandsContract.Command
import tech.antibytes.keather.presentation.ui.store.command.RefreshCommandsContract.CommandReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherStoreFake(
    private val viewModelScope: CoroutineScope,
    vararg state: StoreContract.WeatherDataState,
) : StoreContract.WeatherStore, CommandReceiver {
    private val states = state.toMutableList()

    private val _weatherData: MutableStateFlow<StoreContract.WeatherDataState> = MutableStateFlow(states.removeFirst())
    override val weatherData: StateFlow<StoreContract.WeatherDataState> = _weatherData
    override val error: Flow<StoreContract.WeatherUIError>
        get() = TODO("Not yet implemented")

    override fun refresh() {
        viewModelScope.launch {
            delay(300)
            val state = states.removeFirst()
            _weatherData.update { state }

            if (state == StoreContract.StartUpLoading) {
                refreshAll()
            }
        }
    }

    override fun refreshAll() {
        viewModelScope.launch {
            delay(300)
            val state = states.removeFirst()
            _weatherData.update { state }

            if (state == StoreContract.StartUpLoading) {
                refreshAll()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : CommandReceiver> runCommand(command: Command<T>) = command.execute(this as T)
}
