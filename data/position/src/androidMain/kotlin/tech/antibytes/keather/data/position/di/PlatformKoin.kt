/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.di

import com.google.android.gms.location.LocationServices
import tech.antibytes.keather.data.position.PositionRepositoryContract
import tech.antibytes.keather.data.position.device.Locator
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun resolveLocator(): Module = module {
    factory { LocationServices.getFusedLocationProviderClient(androidContext()) }
    factory<PositionRepositoryContract.Locator> { Locator(get()) }
}
