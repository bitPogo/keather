/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package io.bitpogo.keather.android.app.token

import androidx.compose.ui.graphics.Color

object Color {
    object Surface {
        val global = Core.Secondary.Gray.faded
        val standard = Core.Primary.white
        val strong = Core.Secondary.Gray.medium
        val moderate = Core.Secondary.Gray.light
        val subtle = Core.Secondary.Gray.faded

        object Success {
            val strong = Core.Secondary.Green.medium
            val subtle = Core.Secondary.Green.faded
        }
        val success = Success

        object Info {
            val strong = Core.Secondary.Smoke.medium
            val subtle = Core.Secondary.Smoke.faded
        }
        val info = Info

        object Warning {
            val strong = Core.Primary.Orange.dark
            val subtle = Core.Primary.Orange.faded
        }
        val warning = Warning

        object Danger {
            val strong = Core.Secondary.Red.medium
            val subtle = Core.Secondary.Red.faded
        }
        val danger = Danger
    }
    val surface = Surface

    object Text {
        val standard = Core.Primary.black
        val moderate = Core.Secondary.Gray.dark
        val subtle = Core.Secondary.Gray.medium
        val inverse = Core.Primary.white
        val success = Core.Secondary.Green.medium
        val info = Core.Secondary.Smoke.medium
        val warning = Core.Primary.Orange.dark
        val danger = Core.Secondary.Red.medium
        val accent = Core.Primary.Orange.light
    }
    val text = Text

    val transparent = Color.Transparent

    private object Core {
        object Primary {
            val white = Color(0xFFFFFFFF)
            val black = Color(0xFF000000)
            object Orange {
                val faded = Color(0xFFFFF5ea)
                val light = Color(0xFFfcbc67)
                val dark = Color(0xFFfa9105)
            }
            val orange = Orange
        }
        val primary = Primary

        object Secondary {
            object Gray {
                val faded = Color(0xFFF5F5F5)
                val light = Color(0xFFE6E6E6)
                val medium = Color(0xFFB2B2B2)
                val dark = Color(0xFF767676)
            }
            val gray = Gray

            object Red {
                val faded = Color(0xFFFEF3F3)
                val light = Color(0xFFF78782)
                val medium = Color(0xFFF45750)
                val dark = Color(0xFFEA180F)
            }
            val red = Red

            object Moss {
                val faded = Color(0xFFF5F8F7)
                val light = Color(0xFF9EB8AE)
                val medium = Color(0xFF658A7C)
                val dark = Color(0xFF3A5047)
            }
            val moss = Moss

            object Aqua {
                val faded = Color(0xFFF1F9FA)
                val light = Color(0xFF75C1D0)
                val medium = Color(0xFF0BA3C1)
                val dark = Color(0xFF087A91)
            }
            val aqua = Aqua

            object Berry {
                val faded = Color(0xFFFDF6F8)
                val light = Color(0xFFE8A6B6)
                val medium = Color(0xFFD4496A)
                val dark = Color(0xFF952741)
            }
            val berry = Berry

            object Smoke {
                val faded = Color(0xFFF1F4F6)
                val light = Color(0xFF7390A1)
                val medium = Color(0xFF465B68)
                val dark = Color(0xFF313C49)
            }
            val smoke = Smoke

            object Green {
                val faded = Color(0xFFF2F9F5)
                val light = Color(0xFF7CC199)
                val medium = Color(0xFF077144)
                val dark = Color(0xFF054D2E)
            }
            val green = Green
        }
    }
}
