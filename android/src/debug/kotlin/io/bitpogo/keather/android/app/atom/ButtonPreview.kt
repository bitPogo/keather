/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:function-naming")

package io.bitpogo.keather.android.app.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun ButtonPrimaryEnabledPreview() {
    ButtonPrimaryEnabled(
        onClick = {},
        text = "Ladevorgang stoppen",
    )
}

@Preview
@Composable
internal fun ButtonPrimaryDisabledPreview() {
    ButtonPrimaryDisabled(
        text = "Ladevorgang starten",
    )
}
