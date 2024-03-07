/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.entity

sealed interface ReturnState {
    data object Success : ReturnState
    data object Failure : ReturnState
}
