/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.lib

import kotlin.js.JsName
import kotlin.test.Test
import tech.antibytes.kmock.KMock
import tech.antibytes.kmock.KMockExperimental
import tech.antibytes.kmock.verification.verify
import tech.antibytes.util.test.mustBe

@OptIn(KMockExperimental::class)
@KMock(Dependency::class)
class ThingSpec {
    @Test
    @JsName("fn0")
    fun `A Thing exists`() {
        // Given
        val mock: DependencyMock = kmock()
        mock._doSomethingElse returns "jada"

        val subjectUnderTest = Thing(mock)

        // When
        val actual = subjectUnderTest.doSomething()

        // Then
        actual mustBe 42
        verify {
            mock._doSomethingElse.hasBeenCalled()
        }
    }
}
