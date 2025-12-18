package jp.ac.jec.cm0138.understandme.Presentation.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import jp.ac.jec.cm0138.understandme.Entity.Class
import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.ClassItemView
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.HomeworkItemView
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.HomeScreenViewModel
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.Repository.TestRepo.TestAuthRepository
import jp.ac.jec.cm0138.understandme.Repository.TestRepo.TestClassRepository
import jp.ac.jec.cm0138.understandme.Repository.TestRepo.TestHomeworkRepository
import jp.ac.jec.cm0138.understandme.Repository.TestRepo.TestUserDataRepository
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCase
import jp.ac.jec.cm0138.understandme.UseCase.HomeworkUseCase
import jp.ac.jec.cm0138.understandme.UseCase.UserDataUseCase
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import jp.ac.jec.cm0138.understandme.customTheme.NotoSansJP

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.loadClassesAndHomeworks()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            HomeScreenTopBarContents(
                userName = "24cm0138",
                photoURL = "https://thumbs.dreamstime.com/b/default-profile-picture-avatar-photo-placeholder-vector-illustration-default-profile-picture-avatar-photo-placeholder-vector-189495158.jpg"
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            ClassListBanner(classes = viewModel.classList)
            HomeworkListBanner(homeworks = viewModel.homeworks)
        }
    }
}


@Composable
fun HomeworkListBanner(
    homeworks: List<HomeworkWithStatus>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "提出期限が近い課題",
            style = CustomTypography.header
        )

        Spacer(modifier = Modifier.width(20.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "",
            tint = MyAppTheme.colors.accent.copy(alpha = 0.5f),
        )
    }


    LazyColumn(

    ) {
        items(items = homeworks) { homework ->
            HomeworkItemView(
                id = homework.id,
                title = homework.title,
                dueDate = homework.dueDateString,
                homeworkState = homework.submissionState,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun ClassListBanner(
    classes: List<Class>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "マイクラス",
            style = CustomTypography.header
        )

        Spacer(modifier = Modifier.width(20.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "",
            tint = MyAppTheme.colors.accent.copy(alpha = 0.5f),
        )
    }

    if (!classes.isEmpty()) {
        LazyRow {
            items(items = classes) { classItem ->
                ClassItemView(
                    classID = classItem.id,
                    className = classItem.name,
                    teacherName = classItem.teacherName,
                    showIcon = false,
                    height = 80.dp,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(5.dp)
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.LightGray.copy(alpha = 0.15f))
                .padding(horizontal = 30.dp)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "科目がありません",
                style = TextStyle(
                    fontFamily = NotoSansJP,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.LightGray
            )

        }
    }
}


@Composable
fun HomeScreenTopBarContents(
    userName: String,
    photoURL: String,
    modifier: Modifier = Modifier
) {
    val brush =
        Brush.horizontalGradient(listOf<Color>(MyAppTheme.colors.purple, MyAppTheme.colors.blue))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(brush)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
        ) {
            Text(
                "こんにちは",
                style = CustomTypography.label,
                color = Color.Gray
            )
            Text(
                userName,
                style = CustomTypography.header
            )
        }


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
            modifier = Modifier
                .clip(
                    CircleShape
                )
                .size(50.dp)
                .border(
                    border = BorderStroke(width = 1.dp, color = Color.Gray),
                    shape = CircleShape
                )
        )
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        HomeScreen(
            navController = navController,
            viewModel = HomeScreenViewModel(
                userDataUseCase = UserDataUseCase(
                    userDataRepository = TestUserDataRepository()
                ),
                authRepository = TestAuthRepository(),
                classUseCase = ClassUseCase(classRepository = TestClassRepository()),
                homeworkUseCase = HomeworkUseCase(homeworkRepository = TestHomeworkRepository())
            ),
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}