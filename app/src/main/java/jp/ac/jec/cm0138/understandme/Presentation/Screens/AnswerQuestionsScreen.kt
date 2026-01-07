package jp.ac.jec.cm0138.understandme.Presentation.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import jp.ac.jec.cm0138.understandme.Entity.Choice
import jp.ac.jec.cm0138.understandme.Entity.QuestionWithChoices
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.AnswerMode
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.ArcTimerButton
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.AnswerQuestionsScreenViewModel
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import jp.ac.jec.cm0138.understandme.customTheme.customPrimaryButtonColors
import kotlinx.coroutines.delay

@Composable
fun AnswerQuestionsScreen(
    modifier: Modifier = Modifier,
    homeworkID: String,
    mode: AnswerMode,
    navController: NavController,
    viewModel: AnswerQuestionsScreenViewModel = hiltViewModel()
) {
    var submitted by remember { mutableStateOf(false) }
    val questionsWithChoices = viewModel.questionsWithChoices
    val currentQuestionIndex = viewModel.currentQuestionIndex
    var selectedChoiceID by remember { mutableStateOf<String?>(null) }

    val progress = remember { mutableStateOf(0f) }
    var mainTimerDuration by remember { mutableStateOf(20) }

    LaunchedEffect(Unit) {
        viewModel.loadQuestions(homeworkID = homeworkID)
    }

    // Reset timers when question changes
    LaunchedEffect(currentQuestionIndex) {
        progress.value = 0f
        mainTimerDuration = 20
        submitted = false
        selectedChoiceID = null
    }


    LaunchedEffect(currentQuestionIndex) {
        var remaining = mainTimerDuration

        while (remaining > 0) {
            delay(1000L)
            remaining--
            mainTimerDuration--
        }
        // Time's up, auto-submit with no answer
        if (!submitted) {
            viewModel.postAnswer(
                questionID = questionsWithChoices[currentQuestionIndex].id,
                selectedChoiceID = null,
                homeworkID = homeworkID,
            )
        }
        viewModel.goToNextQuestion(navController)
    }

    if (viewModel.isLoading || questionsWithChoices.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }


    Column(
        modifier = modifier
    ) {
        val currentQuestion = questionsWithChoices[currentQuestionIndex]

        QuestionCard(
            questionWithChoices = currentQuestion,
            onSubmit = {
                viewModel.postAnswer(
                    questionID = currentQuestion.id,
                    homeworkID = homeworkID,
                    selectedChoiceID = selectedChoiceID
                )
                submitted = true
            },
            mode = mode,
            onNextQuestionClick = {
                viewModel.goToNextQuestion(navController)
            },
            submitted = submitted,
            selectedChoiceID = selectedChoiceID,
            onSelect = { selectedChoiceID = it },
            isLastQuestion = currentQuestionIndex == questionsWithChoices.size - 1,
            mainTimerDuration = mainTimerDuration,
            modifier = Modifier.padding(10.dp)
        )



        ArcTimerButton(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            progress = progress,
            durationSeconds = 10,
            label = "Push",
            onComplete = {
                if (!submitted) {
                    viewModel.postAnswer(
                        questionID = questionsWithChoices[currentQuestionIndex].id,
                        selectedChoiceID = null,
                        homeworkID = homeworkID,
                    )
                }
                viewModel.goToNextQuestion(navController)
            })
    }
}


@Composable
fun QuestionCard(
    questionWithChoices: QuestionWithChoices,
    selectedChoiceID: String? = null,
    mainTimerDuration: Int = 20,
    isLastQuestion: Boolean = false,
    mode: AnswerMode,
    onSelect: (String) -> Unit = {},
    submitted: Boolean,
    onSubmit: (String) -> Unit = {},
    onNextQuestionClick: () -> Unit = {},
    userSelectedChoiceID: String? = null, // only for review mode
    modifier: Modifier = Modifier
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(12.dp))
            .background(MyAppTheme.colors.background, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        if (mode == AnswerMode.ANSWER) {
            Text(
                "残り時間: ${mainTimerDuration}秒",
                modifier = Modifier,
                style = CustomTypography.body.copy(
                    color = Color.Red
                )
            )
        } else {
            if (userSelectedChoiceID == null) {
                Text(
                    "未回答の質問",
                    modifier = Modifier,
                    style = CustomTypography.body.copy(
                        color = Color.Red
                    )
                )
            }
        }

        Text(
            questionWithChoices.questionText,
            style = CustomTypography.titleMedium.copy(fontSize = 20.sp),
            modifier = Modifier.padding(vertical = 10.dp)
        )



        questionWithChoices.choices.forEach {
            val isChoiceSelected: Boolean = if (mode == AnswerMode.ANSWER) {
                selectedChoiceID == it.id
            } else {
                userSelectedChoiceID == it.id
            }
            ChoiceButton(
                choice = it,
                isSelected = isChoiceSelected,
                submitted = submitted,
                onSelect = {
                    if (mode == AnswerMode.ANSWER && !submitted) {
                        onSelect(it.id)
                    }
                }, modifier = Modifier.padding(vertical = 4.dp)
            )
        }



        if (mode == AnswerMode.ANSWER) {
            Button(
                onClick = {
                    if (selectedChoiceID != null && !submitted) {
                        onSubmit(selectedChoiceID)
                    } else if (submitted) {
                        onNextQuestionClick()
                    }
                },
                enabled = selectedChoiceID != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(50.dp),
                colors = ButtonDefaults.customPrimaryButtonColors(),
                shape = RoundedCornerShape(15.dp)
            ) {
                val buttonText = if (submitted) {
                    if (isLastQuestion) "終了" else "次の質問へ"
                } else {
                    "回答を送信"
                }
                Text(
                    text = buttonText,
                    style = CustomTypography.header.copy(fontSize = 18.sp),
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun ChoiceButton(
    choice: Choice,
    isSelected: Boolean,
    submitted: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isSelected -> MyAppTheme.colors.blue.copy(alpha = 0.2f)
        else -> Color.Gray.copy(alpha = 0.1f)
    }

    val borderColor = if (isSelected) MyAppTheme.colors.blue else Color.Transparent

    Surface(
        onClick = onSelect,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = choice.choiceText,
                style = CustomTypography.label.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            when {
                submitted && choice.isCorrect -> {
                    Icon(
                        modifier = Modifier.size(19.dp),
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Correct",
                        tint = MyAppTheme.colors.secAccent,
                    )
                }

                submitted && isSelected -> {
                    Icon(
                        modifier = Modifier.size(19.dp),
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Wrong",
                        tint = Color.Red
                    )
                }

                !submitted && isSelected -> {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = "Selected",
                        tint = MyAppTheme.colors.blue
                    )
                }

                else -> {
                    // Invisible placeholder (like opacity 0.0001 in SwiftUI)
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        tint = Color.Transparent
                    )
                }
            }
        }
    }
}