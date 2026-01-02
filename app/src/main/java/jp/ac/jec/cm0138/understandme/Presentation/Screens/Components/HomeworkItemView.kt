package jp.ac.jec.cm0138.understandme.Presentation.Screens.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import jp.ac.jec.cm0138.understandme.Entity.HomeworkState
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import jp.ac.jec.cm0138.understandme.customTheme.NotoSansJP

@Composable
fun HomeworkItemView(
    title: String,
    dueDate: String?,
    homeworkState: HomeworkState,
    onTap: () -> Unit = {},
    onAnswerClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onTap,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = title,
                    style = CustomTypography.titleMedium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "",
                        tint = Color.Gray,
                    )

                    val dueDateString: String =
                        if (dueDate != null) dueDate + "日まで" else "締切未設定"

                    Text(
                        text = dueDateString,
                        style = CustomTypography.label,
                        color = Color.Gray
                    )

                }


                val brush = Brush.horizontalGradient(
                    listOf<Color>(
                        MyAppTheme.colors.purple,
                        MyAppTheme.colors.lightBlue
                    )
                )


                Text(
                    text = homeworkState.stateDescription,
                    style = CustomTypography.label.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = if (homeworkState == HomeworkState.generatingQuestions)
                                brush
                            else
                                SolidColor(homeworkState.color.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 6.dp)
                        .padding(horizontal = 12.dp)
                )
            }


            if (homeworkState == HomeworkState.generatingQuestions) {
                LottieView(
                    R.raw.ai,
                    modifier = Modifier
                        .size(80.dp)
                )
            }

            if (homeworkState == HomeworkState.questionGenerated) {
                Surface(
                    onClick = onAnswerClicked
                ) {
                    Text(
                        text = "回答",
                        style = TextStyle(
                            fontFamily = NotoSansJP,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .background(Color.Blue.copy(alpha = 0.3f))
                            .padding(horizontal = 10.dp)
                            .padding(13.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LottieView(
    id: Int,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(id)
    )

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
}


@Preview(showBackground = true)
@Composable
fun HomeworkItemPreview() {
    Scaffold { innerPadding ->
        HomeworkItemView(
            title = "課題名",
            dueDate = null,
            homeworkState = HomeworkState.questionGenerated,
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
        )
    }
}