/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.core.chart.composed.plus
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
    Text("Temperature: ${realtimeData.temperatureInCelsius.temperature}°C")
    Text("Windspeed: ${realtimeData.windSpeedInKilometerPerHour.speed}kmh")
    Text("Precipitation: ${realtimeData.precipitationInMillimeter.precipitation}mm")
}

@Composable
fun LocationDisplay(
    location: Location,
) {
    Text("You are in ${location.name.name} (${location.region.region}/${location.country.country})")
    Spacer(modifier = Modifier.size(14.dp))
}

@Composable
fun RealtimeScreen(
    viewModel: StoreContract.WeatherStore,
) {
    val state = viewModel.weatherData.collectAsState()

    Column {
        if (state.hasValues()) {
            val weatherData = (state.value as StoreContract.WeatherDataContentfulState).data
            LocationDisplay(weatherData.location)
            RealtimeDataDisplay(weatherData.realtimeData)
        }
        if (state.value == StoreContract.StartUpError) {
            Text(
                fontSize = 10.sp,
                text = "Something went wrong!",
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                onClick = viewModel::refreshAll,
            ) {
                Text(
                    fontSize = 10.sp,
                    text = "Refresh All",
                )
            }
            Spacer(modifier = Modifier.size(12.dp))

            Button(
                enabled = state.hasValues(),
                onClick = viewModel::refresh,
            ) {
                Text(
                    fontSize = 10.sp,
                    text = "Refresh Weather",
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
