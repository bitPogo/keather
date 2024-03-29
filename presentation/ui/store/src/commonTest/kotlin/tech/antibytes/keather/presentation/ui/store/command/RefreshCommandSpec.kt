/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.presentation.ui.store.command

import tech.antibytes.keather.presentation.ui.store.kmock
import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.verify
import tech.antibytes.util.test.fulfils

@OptIn(KMockExperimental::class)
@KMock(
    RefreshCommandsContract.Refresh::class,
)
class RefreshCommandSpec {
    @Test
    @JsName("fn1")
    fun `Given execute is called it runs refresh`() {
        // Given
        val command: RefreshMock = kmock(relaxUnitFun = true)

        // When
        RefreshCommand.execute(command)

        // Then
        verify {
            command._refresh.hasBeenCalled()
        }
    }

    @Test
    @JsName("fn0")
    fun `It fulfils Command`() {
        RefreshCommand fulfils RefreshCommandsContract.Command::class
    }
}
