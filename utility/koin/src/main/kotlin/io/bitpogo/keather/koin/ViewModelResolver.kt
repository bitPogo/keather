/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.koin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

// see: https://github.com/InsertKoinIO/koin/blob/7b4bee0c3950bee1b1d13bd466874bf4a3467a78/android/koin-android/src/main/java/org/koin/androidx/viewmodel/GetViewModel.kt#L29
// see: ViewModelProvider#get(Class<ViewModel)
@PublishedApi
internal fun <T : Any> resolveViewModelAdv(
    vmClass: KClass<T>,
    viewModelStore: ViewModelStore,
    extras: CreationExtras,
    qualifier: Qualifier? = null,
    scope: Scope,
    parameters: ParametersDefinition? = null,
): T {
    val modelClassName = vmClass.java.canonicalName
        ?: throw IllegalArgumentException("Local and anonymous classes can not be ViewModels!")

    val factory = KoinViewModelFactory.getInstance(vmClass, scope, qualifier, parameters)
    val provider = ViewModelProvider(viewModelStore, factory, extras)

    @Suppress("UNCHECKED_CAST")
    return if (qualifier != null) {
        provider[qualifier.value, ViewModel::class.java]
    } else {
        provider[
            "$DEFAULT_KEY:$modelClassName",
            ViewModel::class.java,
        ]
    } as T
}

private const val DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey"
