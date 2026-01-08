package jp.ac.jec.cm0138.understandme.Presentation.Screens

import android.R.attr.shape
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.DETAIL_STATS_ROUTE
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.MonthlyAverageScoreChart
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.ProfileScreenViewModel
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.loadProfileData()
    }

    if (viewModel.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item{
            Text(
                text = "プロフィール",
                style = CustomTypography.header,
                modifier = Modifier
                    .padding(horizontal = 72.dp),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            ProfileImage(
                modifier = Modifier.padding(top = 10.dp),
                photoURL = viewModel.userData?.photoURL ?: ""
            )

            Text(
                text = viewModel.userData?.displayName ?: "ゲストユーザー",
                modifier = Modifier,
                style = CustomTypography.header.copy(
                    color = Color.Black,
                    fontSize = 30.sp
                )
            )

            Text(
                text = viewModel.userData?.email ?: "",
                modifier = Modifier,
                style = CustomTypography.body.copy(
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            )


            Text(
                text = "学業進捗",
                modifier = Modifier
                    .padding(top = 30.dp)
                    .fillMaxWidth(),
                style = CustomTypography.header.copy(
                    color = Color.Black,
                    fontSize = 20.sp
                )
            )

            if (viewModel.monthlyAverageResult.isNotEmpty()) {
                MonthlyAverageScoreChart(
                    currentSelectedYear = viewModel.currentSelectedYearForGraph,
                    nextYearButtonClicked = {
                        viewModel.onNextYearButtonClicked()
                    },
                    previousYearButtonClicked = {
                        viewModel.onPreviousYearButtonClicked()
                    },
                    monthlyData = viewModel.monthlyAverageResult,
                    modifier = Modifier.padding(10.dp)
                )
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(200.dp)
                        .background(MyAppTheme.colors.background),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                    Icon(
                        painter = painterResource(R.drawable.nosign),
                        contentDescription = "",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(50.dp)
                    )
                    Text("平均スコアのデータが \n まだありません。",
                        modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                        textAlign = TextAlign.Center,
                        style = CustomTypography.body.copy(
                            fontSize = 16.sp,
                            color = Color.Gray
                        ),
                    )
                    }
                }
            }


            // Legend
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(MyAppTheme.colors.accent, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "平均スコア",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatsButton(
                modifier = Modifier,
                finishedHomeworkCount = viewModel.allResults.size,
                averageScore = viewModel.averageScoreOfAllTime,
                onClick = {
                    navController.navigate(DETAIL_STATS_ROUTE)
                }
            )


            LogoutButton(
                onClick = {
                    viewModel.signOut()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}


@Composable
fun LogoutButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier
            .padding(top = 40.dp)
            .fillMaxWidth()
            .height(50.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.rectangle_portrait_and_arrow_right),
                contentDescription = "Log out",
                tint = Color.Red,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text("ログアウト", style = CustomTypography.label.copy(
                fontSize = 16.sp,
                color = Color.Red
            ))
        }

    }
}
@Composable
fun StatsButton(
    finishedHomeworkCount: Int,
    averageScore: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    painter = painterResource(R.drawable.checkmark_circle),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp),
                    tint = MyAppTheme.colors.secAccent
                )
                Text(
                    text = "$finishedHomeworkCount",
                    style = CustomTypography.label.copy(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "完了した課題",
                    style = CustomTypography.label.copy(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    painter = painterResource(R.drawable.star_hexagon),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp),
                    tint = MyAppTheme.colors.accent
                )
                Text(
                    text = "${averageScore} 点",
                    style = CustomTypography.label.copy(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "平均スコア",
                    style = CustomTypography.label.copy(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )


            }

        }
    }
}


@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    photoURL: String
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoURL)
            .crossfade(true)
            .listener(
                onError = { request, result ->
                    Log.e("AsyncImage", "Error loading image: ${result.throwable}")
                }
            )
            .build(),
        placeholder = painterResource(id = R.drawable.person_fill),
        fallback = painterResource(id = R.drawable.person_fill),
        error = painterResource(id = R.drawable.person_fill),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .clip(
                CircleShape
            )
            .size(150.dp)
      )
}


@Preview("Profile Screen", showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    Scaffold {
        ProfileScreen(
            modifier = Modifier.padding(it),
            navController = navController
        )
    }

}