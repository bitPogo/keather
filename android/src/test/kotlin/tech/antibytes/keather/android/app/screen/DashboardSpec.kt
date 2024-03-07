/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.android.app.screen

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import tech.antibytes.keather.android.app.RoborazziTest
import tech.antibytes.keather.presentation.ui.store.StoreContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class DashboardSpec : RoborazziTest() {
    private val data = StoreContract.UIWeatherData(
        location = StoreContract.UILocation(
            name = "Somwhere",
            region = "Nowhere",
            country = "Moonstate of the Moon",
        ),
        realtimeData = StoreContract.UIRealtimeData(
            "24°C",
            "200kmh",
            "2000mm",
        ),
        forecast = listOf(
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
        ),
        historicData = listOf(
            StoreContract.UIChartData(
                temperature = 13.0,
                precipitation = 2002.0,
            ),
            StoreContract.UIChartData(
                temperature = 16.23,
                precipitation = 123.0,
            ),
            StoreContract.UIChartData(
                temperature = 18.0,
                precipitation = 20.0,
            ),
            StoreContract.UIChartData(
                temperature = 20.0,
                precipitation = 10.0,
            ),
        ),
    )

    @Test
    fun `Given the Store is its initial state it only shows and the initial message`() = runTest {
        // Given
        val states = arrayOf(StoreContract.Initial)
        val viewModel = WeatherStoreFake(this, *states)

        // When
        subjectUnderTest.setContent {
            Dashboard(viewModel)
        }

        // Then
        subjectUnderTest.onNodeWithText("Please hit the refresh button to start")
            .assertExists()
        subjectUnderTest.onNodeWithText("Refresh everything").assertExists()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given the Store is its initial state and the refresh button is hit it displays errors`() = runTest {
        // Given
        val states = arrayOf(StoreContract.Initial, StoreContract.StartUpLoading, StoreContract.StartUpError)
        val viewModel = WeatherStoreFake(this, *states)

        // When
        subjectUnderTest.setContent {
            Dashboard(viewModel)
        }
        subjectUnderTest.onNodeWithText("Refresh everything").performClick()
        advanceTimeBy(400)

        // Then
        subjectUnderTest.onNodeWithText("Please hit the refresh button to start").assertExists()

        advanceUntilIdle()
        subjectUnderTest.onNodeWithText("Please hit the refresh button to start").assertDoesNotExist()
        subjectUnderTest.onNodeWithText("Something went wrong").assertExists()
        subjectUnderTest.onRoot().captureRoboImage()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given the Store is its initial state and the refresh button is hit it displays data`() = runTest {
        // Given
        val states = arrayOf(StoreContract.Initial, StoreContract.StartUpLoading, StoreContract.Loaded(data))
        val viewModel = WeatherStoreFake(this, *states)

        // When
        subjectUnderTest.setContent {
            Dashboard(viewModel)
        }
        subjectUnderTest.onNodeWithText("Refresh everything").performClick()
        advanceTimeBy(400)

        // Then
        subjectUnderTest.onNodeWithText("Please hit the refresh button to start").assertExists()

        advanceUntilIdle()
        subjectUnderTest.onNodeWithText("Please hit the refresh button to start").assertDoesNotExist()
        subjectUnderTest.onNodeWithText("Your realtime Data Overview").assertExists()
        subjectUnderTest.onRoot().captureRoboImage()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given the Store is its initial state and the refresh button is hit it keeps displays data`() = runTest {
        // Given
        val states = arrayOf(StoreContract.Loaded(data), StoreContract.Loading(data), StoreContract.Error(data))
        val viewModel = WeatherStoreFake(this, *states)

        // When
        subjectUnderTest.setContent {
            Dashboard(viewModel)
        }
        subjectUnderTest.onNodeWithText("Refresh everything").performScrollTo().performClick()
        advanceTimeBy(400)
        subjectUnderTest.onNodeWithText("Your realtime Data Overview").assertExists()

        // Then
        advanceUntilIdle()
        subjectUnderTest.onNodeWithText("Your realtime Data Overview").assertExists()
        subjectUnderTest.onRoot().captureRoboImage()
    }
}
