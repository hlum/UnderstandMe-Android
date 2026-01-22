package jp.ac.jec.cm0138.understandme.Presentation.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.launch

@Composable
fun AnswerQuestionsScreen(
    modifier: Modifier = Modifier,
    homeworkID: String,
    mode: AnswerMode,
    navController: NavController,
    viewModel: AnswerQuestionsScreenViewModel = hiltViewModel()
) {
    val progress = remember { mutableFloatStateOf(0f) }
    var mainTimerDuration by remember { mutableIntStateOf(viewModel.remoteConfigManager.mainTimerDuration) }
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()


    BackHandler(enabled = true) {
        // Do nothing → disables system back during test
    }

    LaunchedEffect(Unit) {
        viewModel.loadQuestions(homeworkID = homeworkID)
    }

    // Reset timers when question changes
    LaunchedEffect(viewModel.currentQuestionIndex) {
        progress.floatValue = 0f
        mainTimerDuration = viewModel.remoteConfigManager.mainTimerDuration
        viewModel.submitted = false
    }


    fun handleTimeOut() {
        if(!viewModel.submitted && !viewModel.isSubmittingAnswer) {
            viewModel.postAnswer(
                questionID = viewModel.questionsWithChoices[viewModel.currentQuestionIndex].id,
                selectedChoiceID = null,
                homeworkID = homeworkID,
            )
//            // Wait for postAnswer to complete, then go to next question
//            coroutineScope.launch {
//                delay(100L) // Small delay to allow UI to update
//                viewModel.goToNextQuestion(navController, homeworkID)
//            }
        }
    }


    LaunchedEffect(viewModel.currentQuestionIndex, viewModel.timerRunning) {
        var remaining = mainTimerDuration

        while (remaining > 0 && viewModel.timerRunning) {
            delay(1000L)
            remaining--
            mainTimerDuration--
        }
        // Time's up, auto-submit with no answer and go to next
        if (remaining == 0) {
            handleTimeOut()
//            if(!submitted && !viewModel.isSubmittingAnswer) {
//                viewModel.postAnswer(
//                    questionID = viewModel.questionsWithChoices[currentQuestionIndex].id,
//                    selectedChoiceID = null,
//                    homeworkID = homeworkID,
//                )
//                // Wait for postAnswer to complete, then go to next question
//                delay(100L) // Small delay to allow UI to update
//                viewModel.goToNextQuestion(navController, homeworkID)
//            }
        }
    }

    if (viewModel.isLoading || viewModel.questionsWithChoices.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .scrollable(
                scrollState,
                orientation = Orientation.Vertical
            )
    ) {
        val currentQuestion = viewModel.questionsWithChoices[viewModel.currentQuestionIndex]

        QuestionCard(
            questionWithChoices = currentQuestion,
            onSubmit = {
                viewModel.postAnswer(
                    questionID = currentQuestion.id,
                    homeworkID = homeworkID,
                    selectedChoiceID =viewModel. selectedChoiceID
                )
            },
            mode = mode,
            onNextQuestionClick = {
                viewModel.goToNextQuestion(navController, homeworkID)
            },
            submitted = viewModel.submitted,
            selectedChoiceID = viewModel.selectedChoiceID,
            onSelect = { viewModel.selectedChoiceID = it },
            isLastQuestion = viewModel.currentQuestionIndex == viewModel.questionsWithChoices.size - 1,
            mainTimerDuration = mainTimerDuration,
            correctChoiceID = viewModel.correctChoiceID,
            isSubmitting = viewModel.isSubmittingAnswer,
            modifier = Modifier.padding(10.dp)
        )



        ArcTimerButton(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            progress = progress,
            durationSeconds = viewModel.remoteConfigManager.arcTimerDuration,
            label = "Push",
            timerRunning = viewModel.timerRunning,
            onComplete = {
                handleTimeOut()
//                if (!submitted && !viewModel.isSubmittingAnswer) {
//                    viewModel.postAnswer(
//                        questionID = viewModel.questionsWithChoices[currentQuestionIndex].id,
//                        selectedChoiceID = null,
//                        homeworkID = homeworkID,
//                    )
//                   // After submitting, automatically go to next question
//                    coroutineScope.launch {
//                        delay(100L) // Small delay to allow UI to update
//                        viewModel.goToNextQuestion(navController, homeworkID)
//                    }
//                }
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
    isSubmitting: Boolean,
    onSelect: (String) -> Unit = {},
    submitted: Boolean,
    onSubmit: (String) -> Unit = {},
    onNextQuestionClick: () -> Unit = {},
    userSelectedChoiceID: String? = null, // only for review mode
    correctChoiceID: String? = null, // correct choice ID from server
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
                correctChoiceID = correctChoiceID,
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
                    if (!submitted && selectedChoiceID != null && !isSubmitting) {
                        onSubmit(selectedChoiceID)
                    } else if (submitted && !isSubmitting) {
                        onNextQuestionClick()
                    }
                },
                enabled = ((!submitted && selectedChoiceID != null) || submitted) && !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(50.dp),
                colors = ButtonDefaults.customPrimaryButtonColors(),
                shape = RoundedCornerShape(15.dp)
            ) {
                if (isSubmitting) {
                    Row(
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "送信中...",
                            style = CustomTypography.header.copy(fontSize = 18.sp),
                            color = Color.White
                        )
                    }
                } else {
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
}


@Composable
fun ChoiceButton(
    choice: Choice,
    isSelected: Boolean,
    submitted: Boolean,
    correctChoiceID: String? = null,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCorrect = correctChoiceID != null && choice.id == correctChoiceID

    val backgroundColor = when {
        submitted && isCorrect -> MyAppTheme.colors.secAccent.copy(alpha = 0.2f)
        submitted && isSelected && !isCorrect -> Color.Red.copy(alpha = 0.2f)
        isSelected -> MyAppTheme.colors.blue.copy(alpha = 0.2f)
        else -> Color.Gray.copy(alpha = 0.1f)
    }

    val borderColor = when {
        submitted && isCorrect -> MyAppTheme.colors.secAccent
        submitted && isSelected && !isCorrect -> Color.Red
        isSelected -> MyAppTheme.colors.blue
        else -> Color.Transparent
    }

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
                submitted && isCorrect -> {
                    Icon(
                        modifier = Modifier.size(19.dp),
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Correct",
                        tint = MyAppTheme.colors.secAccent,
                    )
                }

                submitted && isSelected && !isCorrect -> {
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