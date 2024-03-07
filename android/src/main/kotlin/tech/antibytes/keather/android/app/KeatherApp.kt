/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.android.app

import android.app.Application
import tech.antibytes.keather.android.app.di.resolveApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

open class KeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@KeatherApp)
            modules(resolveApp())
        }
    }
}
