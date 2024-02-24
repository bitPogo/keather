/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.http.networking.error

sealed class HttpError(
    message: String? = null,
    cause: Throwable? = null,
) : Error(message, cause) {
    class NoConnection : HttpError()
    class RequestError(val status: Int) : HttpError()
    class RequestValidationFailure(message: String) : HttpError(message)
    class ResponseTransformFailure : HttpError(message = "Unexpected Response")
}
