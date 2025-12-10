package jp.ac.jec.cm0138.understandme.customTheme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class MyColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val accent: Color,
    val secAccent: Color
)


val darkColor = MyColors(
    primary = Color(0xFF171717),
    secondary = Color(0xFF7C7C7C),
    background = Color(0xFFFFFFFF),
    accent = Color(0xFF5856D6),
    secAccent = Color(0xFF08F2A0)
)

val lightColor = MyColors(
    primary = Color(0xFF171717),
    secondary = Color(0xFF7C7C7C),
    background = Color(0xFFFFFFFF),
    accent = Color(0xFF5856D6),
    secAccent = Color(0xFF08F2A0)
)

val LocalMyColors = staticCompositionLocalOf {
    lightColor
}