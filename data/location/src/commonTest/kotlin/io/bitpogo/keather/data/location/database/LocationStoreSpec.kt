/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.data.location.database

import io.bitpogo.keather.data.location.LocationRepositoryContract
import io.bitpogo.keather.data.location.model.store.SaveableLocation
import io.bitpogo.keather.entity.Country
import io.bitpogo.keather.entity.Latitude
import io.bitpogo.keather.entity.Longitude
import io.bitpogo.keather.entity.Name
import io.bitpogo.keather.entity.Region
import kotlin.js.JsName
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.coroutine.AsyncTestReturnValue
import tech.antibytes.util.test.coroutine.clearBlockingTest
import tech.antibytes.util.test.coroutine.resolveMultiBlockCalls
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

// Note since sqldelight 2.x the QueryInterfaces are gone...which is bad
@RobolectricConfig(manifest = "--none")
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class LocationStoreSpec {
    private val fixture = kotlinFixture()
    private val db = DatabaseDriver()

    @BeforeTest
    fun setup() {
        clearBlockingTest()
        runBlockingTest {
            db.open()
        }
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    @JsName("fn0")
    fun `Given fetch Location is called it fetches the latest stored Location`(): AsyncTestReturnValue {
        runBlockingTest {
            // Given
            val longitude: Double = fixture.fixture()
            val latitude: Double = fixture.fixture()
            val name: String = fixture.fixture()
            val country: String = fixture.fixture()
            val region: String = fixture.fixture()

            db.dataBase.positionQueries.set(longitude, latitude)
            db.dataBase.locationQueries.set(
                longitude = longitude,
                latitude = latitude,
                name = name,
                region = region,
                country = country,
            )

            // When
            val location = LocationStore(db.dataBase.locationQueries).fetchLocation()

            // Then
            location.getOrNull() mustBe SaveableLocation(
                longitude = Longitude(longitude),
                latitude = Latitude(latitude),
                name = Name(name),
                region = Region(region),
                country = Country(country),
            )
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn1")
    fun `Given fetch Location is called and no location is store it propagates an Error`(): AsyncTestReturnValue {
        runBlockingTest {
            // When
            val location = LocationStore(db.dataBase.locationQueries).fetchLocation()

            // Then
            location.exceptionOrNull() fulfils IllegalStateException::class
        }

        return resolveMultiBlockCalls()
    }

    @Test
    @JsName("fn3")
    fun `It fulfils Store`(): AsyncTestReturnValue {
        runBlockingTest {
            LocationStore(db.dataBase.locationQueries) fulfils LocationRepositoryContract.Store::class
        }

        return resolveMultiBlockCalls()
    }
}
