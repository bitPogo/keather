/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.http.networking.plugin

import io.bitpogo.keather.http.kmock
import io.bitpogo.keather.serialization.JsonConfiguratorContract
import io.bitpogo.keather.serialization.JsonConfiguratorContractMock
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlin.js.JsName
import kotlin.test.Test
import kotlinx.serialization.json.JsonBuilder
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(
    JsonConfiguratorContract::class,
)
class SerializerConfiguratorSpec {
    @Test
    @JsName("fn0")
    fun `It fulfils SerializerConfigurator`() {
        SerializerConfigurator fulfils KtorPluginsContract.SerializerConfigurator::class
    }

    @Test
    @JsName("fn1")
    fun `Given configure is called with a JsonFeatureConfig it just runs while configuring the serializer`() {
        // Given
        val pluginConfig = ContentNegotiation.Config()
        val jsonConfigurator: JsonConfiguratorContractMock = kmock()

        var capturedBuilder: JsonBuilder? = null
        jsonConfigurator._configure run { delegatedBuilder ->
            capturedBuilder = delegatedBuilder
            delegatedBuilder
        }

        // When
        val result = SerializerConfigurator.configure(pluginConfig, jsonConfigurator)

        // Then
        result mustBe Unit
        capturedBuilder!! fulfils JsonBuilder::class
    }
}
