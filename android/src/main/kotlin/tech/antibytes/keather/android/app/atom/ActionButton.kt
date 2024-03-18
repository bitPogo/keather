/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.android.app.atom

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import tech.antibytes.keather.android.app.token.Color
import tech.antibytes.keather.android.app.token.Font

@Composable
private fun TextButtonEnabled(text: String) {
    Text(
        text = text,
        color = Color.text.inverse,
        style = Font.text.regular.s,
    )
}

@Composable
private fun TextButtonDisabled(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.text.regular.s,
    )
}

enum class ActionButtonState {
    ENABLED,
    DISABLED,
}

@Composable
fun ActionButton(
    label: String,
    state: ActionButtonState = ActionButtonState.ENABLED,
    onClick: () -> Unit,
) {
    val isEnabled = state == ActionButtonState.ENABLED

    Button(
        enabled = isEnabled,
        onClick = onClick,
    ) {
        if (isEnabled) {
            TextButtonEnabled(label)
        } else {
            TextButtonDisabled(label)
        }
    }
}

@Composable
fun ActionButton2(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(
            text = text,
            color = Color.text.warning
        )
    }
}
