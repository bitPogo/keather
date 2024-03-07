/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.data.weather.model.api

import tech.antibytes.keather.entity.Latitude
import tech.antibytes.keather.entity.Longitude
import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.test.mustBe

class RequestPositionSpec {
    private val fixture = kotlinFixture()

    @Test
    @JsName("fn0")
    fun `Given a RequestPosition it serializes always to latitude longitude`() {
        // Given
        val location = RequestPosition(Longitude(fixture.fixture()), Latitude(fixture.fixture()))

        // When
        val actual = location.toString()

        // Then
        actual mustBe "${location.latitude.lat},${location.longitude.long}"
    }
}
