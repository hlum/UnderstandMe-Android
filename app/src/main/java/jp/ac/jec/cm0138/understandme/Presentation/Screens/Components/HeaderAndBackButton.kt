
package jp.ac.jec.cm0138.understandme.Presentation.Screens.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme

@Composable
fun HeaderAndBackButton(
    headerTitle:String,
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Surface(
            onClick = onBackButtonClicked,
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "戻る",
                    tint = MyAppTheme.colors.accent
                )
                Text("戻る",
                    style = CustomTypography.body.copy(
                        fontWeight = FontWeight.Bold,
                        color = MyAppTheme.colors.accent
                    )
                )
            }
        }

        Text(
            text = headerTitle,
            style = CustomTypography.header,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 72.dp),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}