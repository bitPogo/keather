/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.http.networking

import io.bitpogo.keather.http.networking.error.HttpError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.request

// This is a Test Concern, however KTor is purely testable in this is is a tradeoff
class FakeHttpCall<T>(
    val body: () -> T,
) : NetworkingContract.HttpCall

class HttpCall(
    val httpStatement: HttpStatement,
) : NetworkingContract.HttpCall

suspend inline fun <reified T : Any> NetworkingContract.HttpCall.receive(): T {
    return try {
        if (this is FakeHttpCall<*>) {
            this.body() as T
        } else {
            (this as HttpCall).httpStatement.body<T>()
        }
    } catch (exception: NoTransformationFoundException) {
        throw HttpError.ResponseTransformFailure()
    }
}
