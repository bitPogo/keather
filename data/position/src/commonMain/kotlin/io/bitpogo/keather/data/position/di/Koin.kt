/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.position.di

import io.bitpogo.keather.data.position.PositionRepository
import io.bitpogo.keather.data.position.PositionRepositoryContract
import io.bitpogo.keather.data.position.database.PositionStore
import io.bitpogo.keather.interactor.repository.RepositoryContract
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal expect fun resolveLocator(): Module

fun resolvePositionRepository(): Module = module {
    includes(resolveLocator())

    single<PositionRepositoryContract.Store> { PositionStore(get()) }
    single<RepositoryContract.PositionRepository> {
        PositionRepository(get(named("io")), get(), get())
    }
}
