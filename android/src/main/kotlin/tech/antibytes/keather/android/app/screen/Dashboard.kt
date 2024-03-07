/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.android.app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tech.antibytes.keather.android.app.R
import tech.antibytes.keather.android.app.atom.ActionButton
import tech.antibytes.keather.android.app.atom.ActionButtonState
import tech.antibytes.keather.android.app.atom.SpacerSmall
import tech.antibytes.keather.android.app.atom.SpacerStandard
import tech.antibytes.keather.android.app.atom.TextEmphatic
import tech.antibytes.keather.android.app.atom.TextError
import tech.antibytes.keather.android.app.atom.TextStandard
import tech.antibytes.keather.android.app.molecules.WeatherChart
import tech.antibytes.keather.presentation.ui.store.StoreContract
import tech.antibytes.keather.presentation.ui.store.StoreContract.WeatherStore
import tech.antibytes.keather.presentation.ui.store.command.RefreshAllCommand
import tech.antibytes.keather.presentation.ui.store.command.RefreshCommand
import tech.antibytes.keather.presentation.ui.store.command.RefreshCommandsContract.Command
import tech.antibytes.keather.presentation.ui.store.command.RefreshCommandsContract.CommandReceiver

// Note this could be done with a lovely formatter (aka icu)
@Composable
fun RealtimeDataDisplay(
    realtimeData: StoreContract.UIRealtimeData,
) {
    TextEmphatic(stringResource(R.string.dashboard_header))
    TextStandard(stringResource(R.string.realtime_temperature, realtimeData.temperature))
    TextStandard(stringResource(R.string.realtime_speed, realtimeData.windSpeed))
    TextStandard(stringResource(R.string.realtime_precipitation, realtimeData.precipitation))
}

@Composable
fun LocationDisplay(
    location: StoreContract.UILocation,
) {
    TextEmphatic(
        stringResource(
            R.string.location,
            location.name,
            location.region,
            location.country,
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
        SpacerSmall()
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
        modifier = Modifier.verticalScroll(rememberScrollState()),
    ) {
        val state = viewmodel.weatherData.collectAsState()
        val hasDisplayableData = state.value is StoreContract.WeatherDataContentfulState

        when (state.value) {
            is StoreContract.WeatherDataContentfulState -> {
                val weatherData = (state.value as StoreContract.WeatherDataContentfulState).data
                LocationDisplay(weatherData.location)
                SpacerStandard()
                RealtimeDataDisplay(weatherData.realtimeData)
                SpacerStandard()
                TextEmphatic(stringResource(R.string.forecast_header))
                WeatherChart(weatherData.forecast)
                SpacerStandard()
                TextEmphatic(stringResource(R.string.history_header))
                WeatherChart(weatherData.historicData)
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
