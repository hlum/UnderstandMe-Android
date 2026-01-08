package jp.ac.jec.cm0138.understandme.Presentation.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.AnswerMode
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.HeaderAndBackButton
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.ResultConfirmationScreenViewModel

@Composable
fun ResultConfirmationScreen(
    viewModel: ResultConfirmationScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavController,
    homeworkID: String
) {

    LaunchedEffect(homeworkID) {
        viewModel.loadAnswers(homeworkID)
    }

    if (viewModel.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
    ) {
        HeaderAndBackButton(
            headerTitle = "結果確認",
            onBackButtonClicked = {
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = viewModel.userAnswers
            ) {
                QuestionCard(
                    questionWithChoices = it.questionsAndChoices,
                    mode = AnswerMode.REVIEW,
                    userSelectedChoiceID = it.userChoiceID,
                    submitted = true,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Preview("Result Confirmation Screen", showBackground = true, showSystemUi = true)
@Composable
fun ResultConfirmationScreenPreview() {
    var navController = rememberNavController()
    Scaffold {
        ResultConfirmationScreen(
            modifier = Modifier.padding(it),
            navController = navController,
            homeworkID = ""
        )
    }
}