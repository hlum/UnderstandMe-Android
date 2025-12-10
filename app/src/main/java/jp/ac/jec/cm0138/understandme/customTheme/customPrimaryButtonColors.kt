package jp.ac.jec.cm0138.understandme.customTheme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable

@Composable
fun ButtonDefaults.customPrimaryButtonColors(): ButtonColors {
    return buttonColors(
        containerColor = MyAppTheme.colors.accent,
        contentColor = MyAppTheme.colors.background,
        disabledContainerColor = MyAppTheme.colors.secondary,
        disabledContentColor = MyAppTheme.colors.primary
    )
}