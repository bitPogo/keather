/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:function-naming")

package io.bitpogo.keather.repository.locator

import io.bitpogo.keather.locator.AppleLocatorContractProtocol
import io.bitpogo.keather.locator.LocationResultContractProtocol
import kotlin.Boolean
import kotlin.Function1
import kotlin.Suppress
import kotlin.Unit
import kotlinx.cinterop.ExperimentalForeignApi
import platform.darwin.NSObject
import tech.antibytes.kmock.KMockContract
import tech.antibytes.kmock.proxy.NoopCollector
import tech.antibytes.kmock.proxy.ProxyFactory

@OptIn(ExperimentalForeignApi::class)
internal class AppleLocatorContractProtocolMock(
    collector: KMockContract.Collector = NoopCollector,
    @Suppress("UNUSED_PARAMETER")
    spyOn: AppleLocatorContractProtocol? = null,
    freeze: Boolean = false,
    @Suppress("unused")
    private val relaxUnitFun: Boolean = false,
    @Suppress("unused")
    private val relaxed: Boolean = false,
) : AppleLocatorContractProtocol, NSObject() {
    public val _locateWithCallback:
        KMockContract.SyncFunProxy<Unit, (Function1<LocationResultContractProtocol?, Unit>) -> Unit> =
        ProxyFactory.createSyncFunProxy(
            "io.bitpogo.keather.locator.AppleLocatorContractProtocolMock#_locateWithCallback",
            collector = collector,
            freeze = freeze,
        )

    public override fun locateWithCallback(callback: Function1<LocationResultContractProtocol?, Unit>): Unit =
        _locateWithCallback.invoke(callback) {
            useUnitFunRelaxerIf(relaxUnitFun || relaxed)
        }

    public fun _clearMock() {
        _locateWithCallback.clear()
    }
}
