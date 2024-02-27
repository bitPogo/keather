/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.token

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object Shape {
    val cornerRadius = CornerRadius

    object CornerRadius {
        val none = 0.dp
        val subtle = 6.dp
        val moderate = 8.dp
        val strong = 16.dp
        val extreme = 24.dp
        val maximum = Int.MAX_VALUE.dp
    }

    object Card {
        val primary = RoundedCornerShape(8.dp)
    }
    val card = Card

    object Button {
        val primary = RoundedCornerShape(8.dp)
    }
    val button = Button

    object ModalBottomSheet {
        val primary = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        val dragHandle = RoundedCornerShape(3.5.dp)
    }
    val modalBottomSheet = ModalBottomSheet

    object SnackBar {
        val primary = RoundedCornerShape(8.dp)
    }
    val snackBar = SnackBar
}
