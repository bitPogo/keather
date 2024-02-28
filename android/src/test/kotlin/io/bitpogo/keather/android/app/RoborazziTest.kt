/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

abstract class RoborazziTest(
    captureType: RoborazziRule.CaptureType = RoborazziRule.CaptureType.None,
) : KoinTest {
    @get:Rule
    val subjectUnderTest = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(subjectUnderTest, captureType)

    @Before
    fun start() {
        stopKoin()
    }

    @After
    fun capture() {
        subjectUnderTest.onRoot().captureRoboImage()
        subjectUnderTest.activityRule.scenario.recreate()
        stopKoin()
    }
}
