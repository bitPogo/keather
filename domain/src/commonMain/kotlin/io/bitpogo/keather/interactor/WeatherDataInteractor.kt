/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.interactor

import io.bitpogo.keather.entity.LocalizedWeatherData
import io.bitpogo.keather.entity.Position
import io.bitpogo.keather.entity.ReturnState
import io.bitpogo.keather.entity.WeatherError
import io.bitpogo.keather.interactor.repository.RepositoryContract
import io.bitpogo.keather.presentation.interactor.InteractorContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

internal class WeatherDataInteractor(
    private val positionRepository: RepositoryContract.PositionRepository,
    private val weatherRepository: RepositoryContract.WeatherRepository,
    private val locationRepository: RepositoryContract.LocationRepository,
) : InteractorContract.WeatherDataInteractor {

    private val _weatherData: MutableSharedFlow<Result<LocalizedWeatherData>> = MutableSharedFlow()
    override val weatherData: SharedFlow<Result<LocalizedWeatherData>> = _weatherData

    inline fun <T, R : Throwable> Result<T>.mapFailure(
        transform: Throwable.() -> R,
    ): Result<T> {
        return if (isFailure) {
            Result.failure(transform(exceptionOrNull()!!))
        } else {
            this
        }
    }

    private suspend fun buildWeatherData(scope: CoroutineScope): Result<LocalizedWeatherData> {
        val realtime = weatherRepository.fetchRealtimeData(scope)
        val forecast = weatherRepository.fetchForecast(scope)
        val historicData = weatherRepository.fetchHistoricData(scope)
        val location = locationRepository.fetchLocation(scope)

        val failure = awaitAll(realtime, forecast, historicData, location).map { data ->
            if (data.isSuccess) {
                Result.success(ReturnState.Success)
            } else {
                Result.failure(data.exceptionOrNull()!!)
            }
        }.firstOrNull { it.isFailure }

        return if (failure == null) {
            Result.success(
                LocalizedWeatherData(
                    location = location.getCompleted().getOrNull()!!,
                    realtimeData = realtime.getCompleted().getOrNull()!!,
                    history = historicData.getCompleted().getOrNull()!!,
                    forecast = forecast.getCompleted().getOrNull()!!,
                ),
            )
        } else {
            Result.failure(failure.exceptionOrNull()!!)
        }
    }

    private suspend fun Result<Position>.updateData(scope: CoroutineScope) {
        val weatherData = mapCatching { position ->
            weatherRepository.updateWeatherData(position, scope).await().getOrThrow()
        }.mapCatching {
            buildWeatherData(scope).getOrThrow()
        }.mapFailure { WeatherError.PlaceholderError }

        _weatherData.emit(weatherData)
    }

    override fun refreshAll(scope: CoroutineScope) {
        scope.launch {
            positionRepository.refreshPosition(this)
                .await()
                .updateData(this)
        }
    }

    override fun refresh(scope: CoroutineScope) {
        scope.launch {
            positionRepository.fetchPosition(this)
                .await()
                .updateData(this)
        }
    }
}
