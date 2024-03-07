/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.koin

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import org.koin.android.ext.android.getKoinScope
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier

// see: https://github.com/InsertKoinIO/koin/blob/7b4bee0c3950bee1b1d13bd466874bf4a3467a78/android/koin-android/src/main/java/org/koin/androidx/viewmodel/ext/android/FragmentVM.kt#L43
@MainThread
inline fun <reified T : Any> Fragment.viewModelAdv(
    qualifier: Qualifier? = null,
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline parameters: (() -> ParametersHolder)? = null,
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        getViewModelAdv(qualifier, ownerProducer, extrasProducer, parameters)
    }
}

/**
 * Retrieve ViewModel instance for Fragment
 * @param qualifier
 * @param ownerProducer
 * @param extrasProducer
 * @param parameters
 */
@OptIn(KoinInternalApi::class)
@MainThread
@PublishedApi
internal inline fun <reified T : Any> Fragment.getViewModelAdv(
    qualifier: Qualifier? = null,
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline parameters: (() -> ParametersHolder)? = null,
): T {
    return resolveViewModelAdv(
        T::class,
        ownerProducer().viewModelStore,
        extras = extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras,
        qualifier = qualifier,
        parameters = parameters,
        scope = getKoinScope(),
    )
}
