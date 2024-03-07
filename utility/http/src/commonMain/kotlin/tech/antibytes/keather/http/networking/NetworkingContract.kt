/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.http.networking

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpClientPlugin

typealias Header = Map<String, String>
typealias Parameter = Map<String, Any?>
typealias Path = List<String>

interface NetworkingContract {
    fun interface PluginConfigurator<PluginConfiguration : Any, SubConfiguration> {
        fun configure(pluginConfiguration: PluginConfiguration, subConfiguration: SubConfiguration)
    }

    data class Plugin<PluginConfiguration : Any, SubConfiguration>(
        val feature: HttpClientPlugin<*, *>,
        val pluginConfigurator: PluginConfigurator<PluginConfiguration, SubConfiguration>,
        val subConfiguration: SubConfiguration,
    )

    interface ClientConfigurator {
        fun configure(
            httpConfig: HttpClientConfig<*>,
            installers: Set<Plugin<in Any, in Any?>>? = null,
        )
    }

    enum class Method(val value: String) {
        HEAD("head"),
        DELETE("delete"),
        GET("get"),
        POST("post"),
        PUT("put"),
    }

    sealed interface HttpCall

    interface RequestBuilder {
        fun setHeaders(headers: Header): RequestBuilder
        fun setParameter(parameter: Parameter): RequestBuilder
        fun addParameter(parameter: Parameter): RequestBuilder
        fun setBody(body: Any): RequestBuilder

        fun prepare(
            method: Method = Method.GET,
            path: Path = listOf(""),
        ): HttpCall

        companion object {
            val BODYLESS_METHODS = listOf(Method.HEAD, Method.GET)
        }
    }

    interface RequestBuilderFactory {
        fun create(): RequestBuilder
    }

    fun interface ConnectivityManager {
        fun hasConnection(): Boolean
    }

    interface Logger : io.ktor.client.plugins.logging.Logger {
        fun info(message: String)
        fun warn(message: String)
        fun error(exception: Throwable, message: String?)
    }
}
