/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.location.di

import tech.antibytes.keather.data.location.LocationRepository
import tech.antibytes.keather.data.location.LocationRepositoryContract
import tech.antibytes.keather.data.location.database.LocationStore
import tech.antibytes.keather.interactor.repository.RepositoryContract
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun resolveLocationRepository(): Module = module {
    single<LocationRepositoryContract.Store> { LocationStore(get()) }
    single<RepositoryContract.LocationRepository> {
        LocationRepository(get(named("io")), get())
    }
}
