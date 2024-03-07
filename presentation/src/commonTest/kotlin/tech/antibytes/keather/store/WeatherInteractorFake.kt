/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.store

import tech.antibytes.keather.entity.LocalizedWeatherData
import tech.antibytes.keather.presentation.interactor.InteractorContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class WeatherInteractorFake(
    results: List<Result<LocalizedWeatherData>> = listOf(Result.failure(IllegalStateException())),
) : InteractorContract.WeatherDataInteractor {
    constructor(result: Result<LocalizedWeatherData>) : this(listOf(result))

    private var results = results.toMutableList()

    private val _weatherData: MutableSharedFlow<Result<LocalizedWeatherData>> = MutableSharedFlow()
    override val weatherData: SharedFlow<Result<LocalizedWeatherData>> = _weatherData
    val invoked: InvocationType
        get() = _invoked

    private var _invoked: InvocationType = InvocationType.NONE

    fun reset(result: List<Result<LocalizedWeatherData>>) {
        this.results = result.toMutableList()
        _invoked = InvocationType.NONE
    }

    fun reset(result: Result<LocalizedWeatherData>) = reset(listOf(result))

    override fun refreshAll(scope: CoroutineScope) {
        _invoked = InvocationType.REFRESH_ALL
        scope.launch {
            _weatherData.emit(results.removeFirst())
        }
    }

    override fun refresh(scope: CoroutineScope) {
        _invoked = InvocationType.REFRESH
        scope.launch {
            _weatherData.emit(results.removeFirst())
        }
    }

    enum class InvocationType {
        REFRESH,
        REFRESH_ALL,
        NONE,
    }
}
