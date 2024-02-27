/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
@file:Suppress("ktlint:standard:function-naming")

package io.bitpogo.keather.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.core.chart.composed.plus
import io.bitpogo.keather.android.app.atom.TextEmphatic
import io.bitpogo.keather.android.app.atom.TextError
import io.bitpogo.keather.android.app.atom.TextStandard
import io.bitpogo.keather.android.app.token.Spacing
import io.bitpogo.keather.entity.Location
import io.bitpogo.keather.entity.RealtimeData
import io.bitpogo.keather.presentation.ui.store.StoreContract
import org.koin.android.ext.android.getKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                val store = getKoin().get<StoreContract.WeatherStore>()

                RealtimeScreen(viewModel = store)
            }
        }
    }
}

private enum class Routes(val route: String) {
    REALTIME("realtimedate"),
}

private fun State<StoreContract.WeatherDataState>.hasValues(): Boolean {
    return this.value is StoreContract.WeatherDataContentfulState
}

// Note this could be done with a lovely formatter (aka icu)
@Composable
fun RealtimeDataDisplay(
    realtimeData: RealtimeData,
) {
    TextEmphatic(stringResource(R.string.reatime_header))
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
            location.country.country
        )
    )
}

@Composable
fun RealtimeScreen(
    viewModel: StoreContract.WeatherStore,
) {
    val state = viewModel.weatherData.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.hasValues()) {
            val weatherData = (state.value as StoreContract.WeatherDataContentfulState).data
            LocationDisplay(weatherData.location)
            Spacer(modifier = Modifier.size(Spacing.xl))
            RealtimeDataDisplay(weatherData.realtimeData)
        }
        if (state.value == StoreContract.StartUpError) {
            TextError(stringResource(R.string.error))
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                onClick = viewModel::refreshAll,
            ) {
                Text(
                    fontSize = 10.sp,
                    text = stringResource(R.string.button_all),
                )
            }
            Spacer(modifier = Modifier.size(12.dp))

            Button(
                enabled = state.hasValues(),
                onClick = viewModel::refresh,
            ) {
                Text(
                    fontSize = 10.sp,
                    text = stringResource(R.string.button_refresh),
                )
            }
            /*Button(
                enabled = state.hasValues(),
                onClick = viewModel::refreshAll,
            ) {
                Text(
                    fontSize = 10.sp,
                    text = "Show me my forecast"
                )
            }
            Button(
                enabled = state.hasValues(),
                onClick = viewModel::refreshAll,
            ) {
                Text(
                    fontSize = 10.sp,
                    text = "Show me some history"
                )
            }*/
        }
    }
}
