/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.koin

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.viewmodel.CreationExtras
import org.koin.android.ext.android.getKoinScope
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier

/**
 * ViewModel API from ComponentActivity
 *
 * @author Arnaud Giuliani
 */

/**
 * Retrieve Lazy ViewModel instance for ComponentActivity
 * @param qualifier
 * @param extrasProducer
 * @param parameters
 */
@MainThread
inline fun <reified T : Any> ComponentActivity.viewModelAdv(
    qualifier: Qualifier? = null,
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline parameters: (() -> ParametersHolder)? = null,
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        getViewModelAdv(qualifier, extrasProducer, parameters)
    }
}

/**
 * Retrieve ViewModel instance for ComponentActivity
 * @param qualifier
 * @param extrasProducer
 * @param parameters
 */
@OptIn(KoinInternalApi::class)
@MainThread
inline fun <reified T : Any> ComponentActivity.getViewModelAdv(
    qualifier: Qualifier? = null,
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline parameters: (() -> ParametersHolder)? = null,
): T {
    return resolveViewModelAdv(
        T::class,
        viewModelStore,
        extras = extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras,
        qualifier = qualifier,
        parameters = parameters,
        scope = getKoinScope(),
    )
}
