/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.location

import tech.antibytes.keather.data.location.model.store.SaveableLocation
import tech.antibytes.keather.entity.Country
import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Location
import tech.antibytes.keather.entity.Longitude
import tech.antibytes.keather.entity.Name
import tech.antibytes.keather.entity.Region
import tech.antibytes.keather.interactor.repository.RepositoryContract
import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(
    LocationRepositoryContract.Store::class,
)
class LocationRepositorySpec {
    private val store: StoreMock = kmock()
    private val fixture = kotlinFixture()

    @BeforeTest
    fun setUp() {
        store._clearMock()
    }

    @Test
    @JsName("fn1")
    fun `Given fetchLocation it delegates the call to the Store propagates its Result`() = runTest {
        // Given
        val location = SaveableLocation(
            latitude = Latitude(fixture.fixture()),
            longitude = Longitude(fixture.fixture()),
            name = Name(fixture.fixture()),
            country = Country(fixture.fixture()),
            region = Region(fixture.fixture()),
        )

        store._fetchLocation returns Result.success(location)

        // When
        val actual = LocationRepository(
            dispatcher = StandardTestDispatcher(testScheduler),
            store = store,
        ).fetchLocation(this).await()

        // Then
        actual.getOrNull() mustBe Location(
            name = location.name,
            region = location.region,
            country = location.country,
        )
    }

    @Test
    @JsName("fn0")
    fun `It fulfils the LocationRepositoryContract`() {
        LocationRepository(
            StandardTestDispatcher(),
            store,
        ) fulfils RepositoryContract.LocationRepository::class
    }
}
