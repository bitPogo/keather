/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.presentation.ui.store.command

import io.bitpogo.keather.presentation.ui.store.kmock
import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.verify
import tech.antibytes.util.test.fulfils

@OptIn(KMockExperimental::class)
@KMock(
    RefreshCommandsContract.RefreshAll::class,
)
class RefreshAllCommandSpec {
    @Test
    @JsName("fn1")
    fun `Given execute is called it runs refreshAll`() {
        // Given
        val command: RefreshAllMock = kmock(relaxUnitFun = true)

        // When
        RefreshAllCommand.execute(command)

        // Then
        verify {
            command._refreshAll.hasBeenCalled()
        }
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Command`() {
        RefreshAllCommand fulfils RefreshCommandsContract.Command::class
    }
}
