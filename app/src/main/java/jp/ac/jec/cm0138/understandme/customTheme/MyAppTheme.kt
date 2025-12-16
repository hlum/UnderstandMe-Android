package jp.ac.jec.cm0138.understandme.customTheme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable


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
        content
    )
}