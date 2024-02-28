/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.atom

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.bitpogo.keather.android.app.token.Spacing

@Composable
fun SpacerStandard() {
    Spacer(modifier = Modifier.size(Spacing.xl))
}

@Composable
fun SpacerSmall() {
    Spacer(modifier = Modifier.size(Spacing.m))
}
