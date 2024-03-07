/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.http.networking

import tech.antibytes.keather.http.networking.error.HttpError
import tech.antibytes.keather.http.networking.plugin.KtorPluginsContract
import io.ktor.client.plugins.ResponseException

object HttpErrorMapper : KtorPluginsContract.ErrorMapper {
    private fun wrapError(error: Throwable): Throwable {
        return if (error is ResponseException) {
            HttpError.RequestError(error.response.status.value)
        } else {
            error
        }
    }

    override fun mapAndThrow(error: Throwable) {
        throw wrapError(error)
    }
}
