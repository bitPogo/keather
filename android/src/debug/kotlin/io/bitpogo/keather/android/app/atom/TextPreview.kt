/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TextErrorPreview() {
    TextError("A Generic Text")
}

@Preview
@Composable
fun TextStandardPreview() {
    TextStandard("A Generic Text")
}

@Preview
@Composable
fun TextEmphaticPreview() {
    TextEmphatic("A Generic Text")
}
