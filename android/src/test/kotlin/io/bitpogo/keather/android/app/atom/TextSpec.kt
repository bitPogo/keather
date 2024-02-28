/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.atom

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.bitpogo.keather.android.app.RoborazziTest
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
}
