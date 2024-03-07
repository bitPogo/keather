/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.android.app.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tech.antibytes.keather.android.app.molecules.WeatherChart
import tech.antibytes.keather.presentation.ui.store.StoreContract

private val data = listOf(
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
)

@Preview
@Composable
fun WeatherChartPreview() {
    WeatherChart(data)
}
