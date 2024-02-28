/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.screen

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import io.bitpogo.keather.android.app.RoborazziTest
import io.bitpogo.keather.presentation.ui.store.StoreContract
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
        subjectUnderTest.onNodeWithText("Something went wrong").captureRoboImage()
    }
}
