package jp.ac.jec.cm0138.understandme.Presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.HeaderAndBackButton
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme

@Composable
fun DetailStatsScreen(
    navController: NavController, modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item(1) {
            HeaderAndBackButton(
                headerTitle = "詳細統計",
                onBackButtonClicked = { navController.popBackStack() },
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
        items(6) {
            AverageScoreForClassItem(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                className = "数学",
                averageScore = 85,
                homeworkCompleted = 8,
                totalHomework = 10
            )
        }
    }

}


@Composable
fun AverageScoreForClassItem(
    modifier: Modifier = Modifier,
    className: String,
    averageScore: Int,
    homeworkCompleted: Int,
    totalHomework: Int
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .background(MyAppTheme.colors.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        val gradientBrush = Brush.linearGradient(
            start = Offset(x = 0f, y = 0f),
            colors = listOf(MyAppTheme.colors.secAccent, MyAppTheme.colors.accent)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.graduationcap_fill),
                contentDescription = "Detail Stats Icon",
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush = gradientBrush)
                    .padding(5.dp)
            )

            Text(
                text = className,
                style = CustomTypography.header,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        // Stats Content
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Average Score Stat
            Column(
                modifier = Modifier
                    .weight(1f)
                    .shadow(3.dp, RoundedCornerShape(20.dp))
                    .background(MyAppTheme.colors.background)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.star_hexagon),
                        contentDescription = "Average Score Icon",
                        tint = MyAppTheme.colors.accent,
                        modifier = Modifier.size(25.dp)
                    )

                    Text(
                        "平均点",
                        style = CustomTypography.body,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "$averageScore", style = CustomTypography.title.copy(
                            fontSize = 30.sp
                        )
                    )


                    Text(
                        "点",
                        style = CustomTypography.body,
                        modifier = Modifier.padding(start = 3.dp)
                    )

                }
            }

            Spacer(modifier = Modifier.size(10.dp))

            // Homework Completed Stat
            Column(
                modifier = Modifier
                    .weight(1f)
                    .shadow(3.dp, RoundedCornerShape(20.dp))
                    .background(MyAppTheme.colors.background)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.checkmark_circle),
                        contentDescription = "Average Score Icon",
                        tint = MyAppTheme.colors.secAccent,
                        modifier = Modifier.size(25.dp)
                    )

                    Text(
                        "課題数",
                        style = CustomTypography.body,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }


                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        "$homeworkCompleted", style = CustomTypography.title.copy(
                            fontSize = 30.sp
                        )
                    )


                    Text(
                        "/ $totalHomework 個",
                        style = CustomTypography.body,
                        modifier = Modifier.padding(start = 3.dp, bottom = 4.dp)
                    )

                }
            }
        }
    }
}

@Preview("Detail Stats Screen", showBackground = true, showSystemUi = true)
@Composable
fun DetailStatsScreenPreview() {
    val navController = rememberNavController()
    Scaffold {
        DetailStatsScreen(
            modifier = Modifier.padding(it), navController = navController
        )
    }

}