/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather

interface HttpContract {
    fun interface ConnectivityManager {
        fun hasConnection(): Boolean
    }

    interface Logger : io.ktor.client.plugins.logging.Logger {
        fun info(message: String)
        fun warn(message: String)
        fun error(exception: Throwable, message: String?)
    }
}
