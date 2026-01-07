package jp.ac.jec.cm0138.understandme.Presentation.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.ClassItemView
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.ClassListScreenViewModel
import jp.ac.jec.cm0138.understandme.Repository.TestRepo.TestAuthRepository
import jp.ac.jec.cm0138.understandme.Repository.TestRepo.TestClassRepository
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCase
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClassListScreen(
    modifier: Modifier = Modifier,
    viewModel: ClassListScreenViewModel = hiltViewModel(),
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        viewModel.loadClasses()
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            Text(
                text = "科目一覧",
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = CustomTypography.header
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            items(items = viewModel.classes, key = { it.id }) { classItem ->
                ClassItemView(
                    classID = classItem.id,
                    className = classItem.name,
                    teacherName = classItem.teacherName,
                    showIcon = true,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ClassListScreenPreview() {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        ClassListScreen(
            viewModel = ClassListScreenViewModel(authenticationRepository = TestAuthRepository(), classUseCase = ClassUseCase(
                classRepository = TestClassRepository()
            )),
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}