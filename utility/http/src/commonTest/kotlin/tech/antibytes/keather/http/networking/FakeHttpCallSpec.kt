/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.http.networking

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class FakeHttpCallSpec {
    @Test
    @JsName("fn2")
    fun `Given receive is called on a FakeHttpCall it propagates errors`() = runTest {
        // Given
        val error = Error()
        val call = FakeHttpCall { throw error }

        // Then
        assertFailsWith<Error> {
            // When
            call.receive<Any>()
        }
    }

    @Test
    @JsName("fn4")
    fun `Given receive is called on a FakeHttpCall it propagates values`() = runTest {
        // Given
        val data = "Test"

        val call = FakeHttpCall { data }

        // When
        val actual = call.receive<String>()

        // Then
        actual mustBe data
    }

    @Test
    @JsName("fn5")
    fun `It fulfils FakeHttpCallContract`() {
        FakeHttpCall {} fulfils NetworkingContract.HttpCall::class
    }
}
