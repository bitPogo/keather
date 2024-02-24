/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.http.networking

import io.bitpogo.keather.http.networking.error.HttpError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFailsWith
import io.bitpogo.keather.http.networking.plugin.KtorPluginsContract
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs

class HttpErrorMapperSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils ErrorMapper`() {
        HttpErrorMapper() fulfils KtorPluginsContract.ErrorMapper::class
    }

    @Test
    @JsName("fn1")
    fun `Given mapAndThrow is called with a Throwable it rethrows non ResponseException`() {
        // Given
        val throwable = RuntimeException("abc")

        // Then
        val error = assertFailsWith<RuntimeException> {
            // When
            HttpErrorMapper().mapAndThrow(throwable)
        }

        // Then
        error sameAs throwable
    }

    @Test
    @JsName("fn2")
    fun `Given mapAndThrow is called with a Throwable it rethrows it as RequestError which contains a HttpStatusCode`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            expectSuccess = true
            HttpResponseValidator {
                handleResponseExceptionWithRequest { response, _ ->
                    HttpErrorMapper().mapAndThrow(response)
                }
            }

            engine {
                addHandler {
                    respondBadRequest()
                }
            }
        }

        // Then
        val error = assertFailsWith<HttpError.RequestError> {
            client.get("/somewhere").body()
        }

        error.status mustBe HttpStatusCode.BadRequest.value
    }
}
