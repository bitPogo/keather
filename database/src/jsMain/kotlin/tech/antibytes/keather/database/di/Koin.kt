/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.database.di

import tech.antibytes.keather.database.KeatherDB
import org.koin.core.module.Module
import org.koin.dsl.module

// Note...the binds it the better alternative...next time
fun resolveDatabase(
    database: KeatherDB,
): Module {
    return module {
        single {
            database.positionQueries
        }
        single {
            database.locationQueries
        }
        single {
            database.weatherQueries
        }
    }
}
