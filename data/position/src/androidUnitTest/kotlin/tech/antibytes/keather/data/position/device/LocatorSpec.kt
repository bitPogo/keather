/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.position.device

import android.location.Location
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import tech.antibytes.keather.data.position.PositionRepositoryContract
import tech.antibytes.keather.data.position.model.store.SaveablePosition
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs

class LocatorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given getCurrentLocation is called it returns null if the client fails`() = runTest {
        mockkStatic(Tasks::class)

        // Given
        val error = RuntimeException()
        val client: FusedLocationProviderClient = mockk()
        val task: Task<Location> = mockk()

        every { task.exception } returns error
        every { task.addOnSuccessListener(any()) } returns task
        every { task.addOnFailureListener(any()) } answers {
            (this.args[0] as OnFailureListener).onFailure(error)
            task
        }

        every { Tasks.forException<Location>(any()) } returns task
        every { client.getCurrentLocation(any<CurrentLocationRequest>(), any()) } throws error

        // When
        val result = Locator(client).fetchPosition()

        // Then
        result.exceptionOrNull() sameAs error
        verify(exactly = 1) { client.getCurrentLocation(any<CurrentLocationRequest>(), any()) }
        verify { Tasks.forException<Location>(error) }
        unmockkStatic(Tasks::class)
    }

    @Test
    fun `Given getCurrentLocation is called it returns null if the client returns an error`() = runTest {
        // Given
        val error = RuntimeException()
        val client: FusedLocationProviderClient = mockk()
        val task: Task<Location> = mockk()
        val locationRequest = slot<CurrentLocationRequest>()

        every { task.exception } returns error
        every { task.addOnSuccessListener(any()) } returns task
        every { task.addOnFailureListener(any()) } answers {
            (this.args[0] as OnFailureListener).onFailure(error)
            task
        }

        every { client.getCurrentLocation(capture(locationRequest), any()) } returns task

        // When
        val result = Locator(client).fetchPosition()

        // Then
        result.exceptionOrNull() sameAs error
        locationRequest.captured.priority mustBe Priority.PRIORITY_BALANCED_POWER_ACCURACY
        verify(exactly = 1) { client.getCurrentLocation(any<CurrentLocationRequest>(), null) }
    }

    @Test
    fun `Given getCurrentLocation is called it returns an error if the client returns a null location`() = runTest {
        // Given
        val error = RuntimeException()
        val client: FusedLocationProviderClient = mockk()
        val task: Task<Location> = mockk()
        val locationRequest = slot<CurrentLocationRequest>()

        every { task.exception } returns error
        every { task.addOnFailureListener(any()) } returns task
        every { task.addOnSuccessListener(any()) } answers {
            @Suppress("UNCHECKED_CAST")
            (this.args[0] as OnSuccessListener<Location?>).onSuccess(null)
            task
        }

        every { client.getCurrentLocation(capture(locationRequest), any()) } returns task

        // When
        val result = Locator(client).fetchPosition()

        // Then
        result.exceptionOrNull() fulfils NullPointerException::class
        locationRequest.captured.priority mustBe Priority.PRIORITY_BALANCED_POWER_ACCURACY
        verify(exactly = 1) { client.getCurrentLocation(any<CurrentLocationRequest>(), null) }
    }

    @Test
    fun `Given getCurrentLocation is called it returns the last location propagated from the client`() = runTest {
        // Given
        val lat: Double = fixture.fixture()
        val long: Double = fixture.fixture()
        val location: Location = mockk {
            every { latitude } returns lat
            every { longitude } returns long
        }
        val client: FusedLocationProviderClient = mockk()
        val task: Task<Location> = mockk()
        val locationRequest = slot<CurrentLocationRequest>()

        every { task.result } returns location
        every { task.addOnFailureListener(any()) } returns task
        every { task.addOnSuccessListener(any()) } answers {
            @Suppress("UNCHECKED_CAST")
            (this.args[0] as OnSuccessListener<Location>).onSuccess(location)
            task
        }

        every { client.getCurrentLocation(capture(locationRequest), any()) } returns task

        // When
        val result = Locator(client).fetchPosition()

        // Then
        result.getOrNull() mustBe SaveablePosition(
            Longitude(long),
            Latitude(lat),
        )
        locationRequest.captured.priority mustBe Priority.PRIORITY_BALANCED_POWER_ACCURACY
        verify(exactly = 1) { client.getCurrentLocation(any<CurrentLocationRequest>(), null) }
    }

    @Test
    fun `It fulfils the LocatorContract`() {
        Locator(mockk()) fulfils PositionRepositoryContract.Locator::class
    }
}
