/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.bitpogo.keather.android.app.R
import io.bitpogo.keather.android.app.atom.ActionButton
import io.bitpogo.keather.android.app.atom.ActionButtonState
import io.bitpogo.keather.android.app.atom.TextEmphatic
import io.bitpogo.keather.android.app.atom.TextError
import io.bitpogo.keather.android.app.atom.TextStandard
import io.bitpogo.keather.android.app.token.Spacing
import io.bitpogo.keather.entity.Location
import io.bitpogo.keather.entity.RealtimeData
import io.bitpogo.keather.presentation.ui.store.StoreContract
import io.bitpogo.keather.presentation.ui.store.StoreContract.WeatherStore
import io.bitpogo.keather.presentation.ui.store.command.RefreshAllCommand
import io.bitpogo.keather.presentation.ui.store.command.RefreshCommand
import io.bitpogo.keather.presentation.ui.store.command.RefreshCommandsContract.Command
import io.bitpogo.keather.presentation.ui.store.command.RefreshCommandsContract.CommandReceiver

// Note this could be done with a lovely formatter (aka icu)
@Composable
fun RealtimeDataDisplay(
    realtimeData: RealtimeData,
) {
    TextEmphatic(stringResource(R.string.dashboard_header))
    TextStandard(stringResource(R.string.realtime_temperature, realtimeData.temperatureInCelsius.temperature))
    TextStandard(stringResource(R.string.realtime_speed, realtimeData.windSpeedInKilometerPerHour.speed))
    TextStandard(stringResource(R.string.realtime_precipitation, realtimeData.precipitationInMillimeter.precipitation))
}

@Composable
fun LocationDisplay(
    location: Location,
) {
    TextEmphatic(
        stringResource(
            R.string.location,
            location.name.name,
            location.region.region,
            location.country.country,
        ),
    )
}

private fun Boolean.toActionButtonState(): ActionButtonState {
    return if (this) {
        ActionButtonState.ENABLED
    } else {
        ActionButtonState.DISABLED
    }
}

@Composable
private fun ActionGroup(
    executor: (Command<out CommandReceiver>) -> Unit,
    enabledSimpleRefresh: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ActionButton(
            stringResource(R.string.button_all),
            onClick = { executor(RefreshAllCommand) },
        )
        Spacer(modifier = Modifier.size(Spacing.m))
        ActionButton(
            stringResource(R.string.button_refresh),
            state = enabledSimpleRefresh.toActionButtonState(),
            onClick = { executor(RefreshCommand) },
        )
    }
}

@Composable
fun Dashboard(
    viewmodel: WeatherStore,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val state = viewmodel.weatherData.collectAsState()
        val hasDisplayableData = state.value is StoreContract.WeatherDataContentfulState

        when (state.value) {
            is StoreContract.WeatherDataContentfulState -> {
                val weatherData = (state.value as StoreContract.WeatherDataContentfulState).data
                LocationDisplay(weatherData.location)
                Spacer(modifier = Modifier.size(Spacing.xl))
                RealtimeDataDisplay(weatherData.realtimeData)
            }
            StoreContract.StartUpError -> {
                TextError(stringResource(R.string.error))
            }
            else -> {
                TextEmphatic(stringResource(R.string.dashboard_not_initialized))
            }
        }

        ActionGroup(executor = { viewmodel.runCommand(it) }, enabledSimpleRefresh = hasDisplayableData)
    }
}
