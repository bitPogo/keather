/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.molecule

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.bitpogo.keather.android.app.RoborazziTest
import io.bitpogo.keather.android.app.molecules.WeatherChart
import io.bitpogo.keather.presentation.ui.store.StoreContract
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class WeatherChartSpec : RoborazziTest() {
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

    @Test
    fun `It renders a Weatherchart`() {
        subjectUnderTest.setContent {
            WeatherChart(data = data)
        }
    }
}
