/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.device

import android.annotation.SuppressLint
import android.location.Location as DTO
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import tech.antibytes.keather.data.position.PositionRepositoryContract
import tech.antibytes.keather.data.position.model.store.SaveablePosition
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class Locator(
    private val client: FusedLocationProviderClient,
) : PositionRepositoryContract.Locator {
    private fun Task<DTO>.onFailure(
        continuation: Continuation<Result<DTO>>,
    ): Task<DTO> = addOnFailureListener {
        continuation.resume(Result.failure(it))
    }

    private fun Task<DTO>.onSuccess(
        continuation: Continuation<Result<DTO>>,
    ): Task<DTO> = addOnSuccessListener { value ->
        continuation.resume(Result.success(value))
    }

    @SuppressLint("MissingPermission")
    private fun FusedLocationProviderClient.fetchLocation(
        action: FusedLocationProviderClient.() -> Task<DTO>,
    ): Task<DTO> {
        return try {
            action()
        } catch (error: Exception) {
            Tasks.forException(error)
        }
    }

    private suspend fun fetchLocation(
        action: FusedLocationProviderClient.() -> Task<DTO>,
    ): Result<DTO> {
        return suspendCoroutine { continuation ->
            client.fetchLocation(action)
                .onFailure(continuation)
                .onSuccess(continuation)
        }
    }

    private fun toLocation(wrappedDto: Result<DTO>): Result<SaveablePosition> {
        return wrappedDto.mapCatching { dto ->
            SaveablePosition(
                latitude = Latitude(dto.latitude),
                longitude = Longitude(dto.longitude),
            )
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun fetchPosition(): Result<SaveablePosition> {
        return fetchLocation {
            getCurrentLocation(LOCATION_REQUEST, null)
        }.run(::toLocation)
    }

    private companion object {
        val LOCATION_REQUEST = CurrentLocationRequest.Builder().apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()
    }
}
