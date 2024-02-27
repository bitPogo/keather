/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.atom

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import io.bitpogo.keather.android.app.RoborazziTest
import io.bitpogo.keather.android.app.roborazziOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class TextSpec : RoborazziTest() {
    @Test
    fun `It renders TextError`() {
        subjectUnderTest.setContent {
            TextError("Error")
        }
    }

    @Test
    fun `It renders TextStandard`() {
        subjectUnderTest.setContent {
            TextStandard("Text")
        }
    }

    @Test
    fun `It renders TextEmphatic`() {
        subjectUnderTest.setContent {
            TextEmphatic("Headline")
        }
    }

    @Test
    fun `It renders TextDiscreet`() {
        subjectUnderTest.setContent {
            TextDiscreet("Small text")
        }
    }
}
