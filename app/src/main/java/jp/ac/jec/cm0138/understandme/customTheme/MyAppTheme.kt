package jp.ac.jec.cm0138.understandme.customTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


object MyAppTheme {
    val colors: MyColors
    @Composable
    @ReadOnlyComposable
    get() = LocalMyColors.current
}


@Composable
fun MyAppTheme(
    dark: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if(dark) {
        darkColor
    } else {
        lightColor
    }


    CompositionLocalProvider(
        LocalMyColors provides colors,
        content = content
    )
}