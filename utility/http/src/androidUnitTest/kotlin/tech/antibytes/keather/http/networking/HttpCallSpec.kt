/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.http.networking

import tech.antibytes.keather.http.networking.error.HttpError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.statement.HttpStatement
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import org.junit.Test
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.ktor.KtorMockClientFactory
import tech.antibytes.util.test.mustBe

class HttpCallSpec {
    @Test
    fun `Given receive is called on a HttpCall it propagates errors`() = runTest {
        // Given
        val error = Error()
        val httpStatement: HttpStatement = mockk {
            coEvery { body<Any>() } throws error
        }

        // Then
        assertFailsWith<Error> {
            // When
            HttpCall(httpStatement).receive<Any>()
        }
    }

    @Test
    fun `Given receive is called on a HttpCall it propagates NoTransformationFoundException as ResponseTransformFailure`() = runTest {
        // Given
        val error: NoTransformationFoundException = mockk()
        val httpStatement: HttpStatement = mockk {
            coEvery { body<Any>() } throws error
        }

        // Then
        assertFailsWith<HttpError.ResponseTransformFailure> {
            // When
            HttpCall(httpStatement).receive<Any>()
        }
    }

    @Test
    fun `Given receive is called on a HttpCall it propagates values`() = runTest {
        // Given
        val data = "Test"
        val client = KtorMockClientFactory.createSimpleMockClient(data)
        val builder = RequestBuilderFactory(client, "abc")

        // When
        val actual = builder.create().prepare().receive<String>()

        // Then
        actual mustBe data
    }

    @Test
    fun `It fulfils HttpCallContract`() {
        HttpCall(mockk()) fulfils NetworkingContract.HttpCall::class
    }
}
