/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.http.networking.plugin

import io.bitpogo.keather.LoggerMock
import io.bitpogo.keather.http.kmock
import io.bitpogo.keather.http.networking.NetworkingContract
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs

@OptIn(KMockExperimental::class)
@KMock(
    NetworkingContract.Logger::class,
)
class LoggingConfiguratorSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils LoggingConfigurator`() {
        LoggingConfigurator() fulfils KtorPluginsContract.LoggingConfigurator::class
    }

    @Test
    @JsName("fn1")
    fun `Given configure is called with a LoggingConfig it sets it up and just runs`() {
        // Given
        val config = Logging.Config()
        val logger: LoggerMock = kmock()

        // When
        val result = LoggingConfigurator().configure(config, logger)

        // Then
        result mustBe Unit
        config.level mustBe LogLevel.ALL

        config.logger sameAs logger
    }
}
