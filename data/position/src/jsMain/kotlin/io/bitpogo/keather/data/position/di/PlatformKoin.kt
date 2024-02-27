/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position.di

import io.bitpogo.keather.data.position.PositionRepositoryContract
import io.bitpogo.keather.data.position.device.Locator
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun resolveLocator(): Module = module {
    single<PositionRepositoryContract.Locator> { Locator() }
}
