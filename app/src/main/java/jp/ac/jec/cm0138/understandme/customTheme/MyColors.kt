package jp.ac.jec.cm0138.understandme.customTheme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class MyColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val accent: Color,
    val secAccent: Color,
    val purple: Color,
    val lightBlue: Color,
    val blue: Color
)


val darkColor = MyColors(
    primary = Color(0xFF171717),
    secondary = Color(0xFF7C7C7C),
    background = Color(0xFFFFFFFF),
    accent = Color(0xFF5E52EA),
    secAccent = Color(0xFF18E79B),
    purple = Color(0xFFE6CBF5),
    lightBlue = Color(0xFFBFD7FE),
    blue = Color(0xFF0883F5)
)

val lightColor = MyColors(
    primary = Color(0xFF171717),
    secondary = Color(0xFF7C7C7C),
    background = Color(0xFFFFFFFF),
    accent = Color(0xFF5E52EA),
    secAccent = Color(0xFF18E79B),
    purple = Color(0xFFE6CBF5),
    lightBlue = Color(0xFFBFD7FE),
    blue = Color(0xFF0883F5)
)

val LocalMyColors = staticCompositionLocalOf {
    lightColor
}