/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.keather.android.app.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ActionButtonEnabledPreview() {
    ActionButton("A Generic Text") {}
}

@Preview
@Composable
fun ActionButtonDisabledPreview() {
    ActionButton(
        "A Generic Text",
        ActionButtonState.DISABLED,
    ) {}
}
