/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.database.di

import io.bitpogo.keather.database.DatabaseFactory
import io.bitpogo.keather.database.KeatherDB
import org.koin.core.module.Module
import org.koin.dsl.module

// Note...the binds it the better alternative...next time
fun resolveDatabase(): Module {
    return module {
        single {
            DatabaseFactory.getInstance(get())
        }
        single {
            get<KeatherDB>().positionQueries
        }
        single {
            get<KeatherDB>().locationQueries
        }
        single {
            get<KeatherDB>().weatherQueries
        }
    }
}