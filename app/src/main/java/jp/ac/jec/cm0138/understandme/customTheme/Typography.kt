package jp.ac.jec.cm0138.understandme.customTheme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm0138.understandme.R


val NotoSansJP = FontFamily(
    Font(R.font.notosansjp_thin, FontWeight.Thin),
    Font(R.font.notosansjp_extralight, FontWeight.ExtraLight),
    Font(R.font.notosansjp_light, FontWeight.Light),
    Font(R.font.notosansjp_regular, FontWeight.Normal),
    Font(R.font.notosansjp_medium, FontWeight.Medium),
    Font(R.font.notosansjp_semibold, FontWeight.SemiBold),
    Font(R.font.notosansjp_bold, FontWeight.Bold),
    Font(R.font.notosansjp_extrabold, FontWeight.ExtraBold),
    Font(R.font.notosansjp_black, FontWeight.Black)
)


object CustomTypography {
    val header = TextStyle(
        fontFamily = NotoSansJP,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    val title = TextStyle(
        fontFamily = NotoSansJP,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )

    val titleMedium = TextStyle(
        fontFamily = NotoSansJP,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )

    val body = TextStyle(
        fontFamily = NotoSansJP,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )

    val label = TextStyle(
        fontFamily = NotoSansJP,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
    )
}

