/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:function-naming")

package io.bitpogo.keather.android.app.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import io.bitpogo.keather.android.app.token.Color

@Composable
fun ButtonPrimaryEnabled(
    onClick: () -> Unit,
    text: String,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    // val shiftOffset = isPressed.toSpacing()

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.matchParentSize(),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.transparent,
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Companion.Button,
                    onClick = onClick,
                ),
        ) {
            Text(
                text = text,
                color = Color.text.standard,
            )
        }
    }
}

@Composable
fun ButtonPrimaryDisabled(
    text: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.surface.moderate,
            ),
    ) {
        Text(
            text = text,
            color = Color.text.moderate,
        )
    }
}
