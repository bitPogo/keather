/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.android.app.atom

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import tech.antibytes.keather.android.app.token.Color
import tech.antibytes.keather.android.app.token.Font

@Composable
fun TextError(text: String) {
    Text(
        text = text,
        color = Color.text.danger,
        style = Font.text.bold.m,
    )
}

@Composable
fun TextStandard(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.text.semiBold.m,
    )
}

@Composable
fun TextEmphatic(text: String) {
    Text(
        text = text,
        color = Color.text.standard,
        style = Font.text.bold.l,
    )
}
