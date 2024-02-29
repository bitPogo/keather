/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.atom

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.bitpogo.keather.android.app.RoborazziTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import tech.antibytes.util.test.mustBe

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class ButtonSpec : RoborazziTest() {
    @Test
    fun `It renders an enabled ActionButton`() {
        subjectUnderTest.setContent {
            ActionButton("Click me") {}
        }
    }

    @Test
    fun `It renders an disabled ActionButton`() {
        subjectUnderTest.setContent {
            ActionButton(
                "Click me",
                ActionButtonState.DISABLED,
            ) {}
        }
    }

    @Test
    fun `Given Click is called it propagates the given action`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            ActionButton("Click me") {
                hasBeenCalled = true
            }
        }

        subjectUnderTest.onNodeWithText("Click me").performClick()

        hasBeenCalled mustBe true
    }

    @Test
    fun `Given Click is called it does not propagate the given action if the Button is disabled`() {
        var hasBeenCalled = false

        subjectUnderTest.setContent {
            ActionButton(
                "Click me",
                ActionButtonState.DISABLED,
            ) {
                hasBeenCalled = true
            }
        }

        subjectUnderTest.onNodeWithText("Click me").performClick()

        hasBeenCalled mustBe false
    }
}
