/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.koin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import org.koin.androidx.viewmodel.parameter.AndroidParametersHolder
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

// see: https://github.com/InsertKoinIO/koin/blob/7b4bee0c3950bee1b1d13bd466874bf4a3467a78/android/koin-android/src/main/java/org/koin/androidx/viewmodel/factory/KoinViewModelFactory.kt#L16

internal class KoinViewModelFactory<R : Any> private constructor(
    private val kClass: KClass<out R>,
    private val scope: Scope,
    private val qualifier: Qualifier?,
    private val params: ParametersDefinition?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val androidParams = AndroidParametersHolder(params, extras)
        val viewModel: R = scope.get(kClass, qualifier) { androidParams }

        @Suppress("UNCHECKED_CAST")
        return if (viewModel is ViewModel) {
            viewModel
        } else {
            throw IllegalArgumentException("The given Type is not bounded to a ViewModel!")
        } as T
    }

    companion object {
        fun <R : Any> getInstance(
            kClass: KClass<out R>,
            scope: Scope,
            qualifier: Qualifier? = null,
            params: ParametersDefinition? = null,
        ): ViewModelProvider.Factory = KoinViewModelFactory(
            kClass = kClass,
            scope = scope,
            qualifier = qualifier,
            params = params,
        )
    }
}
