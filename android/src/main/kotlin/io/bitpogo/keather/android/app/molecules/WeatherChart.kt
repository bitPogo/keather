/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import io.bitpogo.keather.android.app.R
import io.bitpogo.keather.android.app.atom.SpacerSmall
import io.bitpogo.keather.android.app.atom.SpacerStandard
import io.bitpogo.keather.android.app.atom.TextStandard
import io.bitpogo.keather.presentation.ui.store.StoreContract

@Composable
fun WeatherChart(
    data: List<StoreContract.UIChartData>,
) {
    val temperatures: MutableList<FloatEntry> = mutableListOf()
    val precipitations: MutableList<FloatEntry> = mutableListOf()

    data.forEachIndexed { index, (temperature, precipitation) ->
        temperatures.add(
            entryOf(index + 1, temperature),
        )
        precipitations.add(
            entryOf(index + 1, precipitation),
        )
    }
    val columnChart = columnChart()
    val lineChart = lineChart()

    TextStandard(stringResource(R.string.chart_temperature))
    SpacerSmall()
    Chart(
        chart = remember(columnChart) { columnChart },
        model = entryModelOf(temperatures),
        startAxis = rememberStartAxis(
            title = stringResource(R.string.chart_axis_days),
        ),
        bottomAxis = rememberBottomAxis(
            title = stringResource(R.string.chart_axis_temperature),
        ),
    )
    SpacerStandard()
    TextStandard(stringResource(R.string.chart_precipitations))
    SpacerSmall()
    Chart(
        chart = remember(lineChart) { lineChart },
        model = entryModelOf(precipitations),
        startAxis = rememberStartAxis(
            title = stringResource(R.string.chart_axis_days),
        ),
        bottomAxis = rememberBottomAxis(
            title = stringResource(R.string.chart_axis_precipitations),
        ),
    )
}
