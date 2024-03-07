/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.di

import tech.antibytes.keather.data.position.PositionRepository
import tech.antibytes.keather.data.position.PositionRepositoryContract
import tech.antibytes.keather.data.position.database.PositionStore
import tech.antibytes.keather.interactor.repository.RepositoryContract
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
