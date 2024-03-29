/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
@file:Suppress("ktlint:standard:function-naming")

package tech.antibytes.keather.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import tech.antibytes.keather.android.app.screen.Dashboard
import tech.antibytes.keather.koin.viewModelAdv
import tech.antibytes.keather.presentation.ui.store.StoreContract

class MainActivity : ComponentActivity() {
    private val store by viewModelAdv<StoreContract.WeatherStore>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                Dashboard(store)
            }
        }
    }
}
