/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.token

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

object Font {
    object Headline {
        private val base = TextStyle(
            fontWeight = FontWeight.Medium,
        )
        val l = base.copy(
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = (-0.004).em,
        )
        val m = base.copy(
            fontSize = 24.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.002).em,
        )
        val s = base.copy(
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.006).em,
        )
    }
    val headline = Headline

    object Accent {
        val text = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.003).em,
        )
    }
    val accent = Accent

    object Text {
        private val base = TextStyle()
        private val baseL = base.merge(
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.002).em,
            ),
        )

        private val baseM = base.merge(
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 22.sp,
                letterSpacing = (-0.008).em,
            ),
        )

        private val baseS = base.merge(
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 20.sp,
                letterSpacing = (-0.007).em,
            ),
        )

        object Regular {
            private val base = TextStyle(
                fontWeight = FontWeight.Normal,
            )
            val l = base.merge(baseL)
            val m = base.merge(baseM)
            val s = base.merge(baseS)
        }
        val regular = Regular

        object Bold {
            private val base = TextStyle(
                fontWeight = FontWeight.Bold,
            )
            val l = base.merge(baseL)
            val m = base.merge(baseM)
            val s = base.merge(baseS)
        }
        val bold = Bold

        object SemiBold {
            private val base = TextStyle(
                fontWeight = FontWeight.SemiBold,
            )
            val l = base.merge(baseL)
            val m = base.merge(baseM)
            val s = base.merge(baseS)
        }
        val semiBold = SemiBold
    }
    val text = Text
}
