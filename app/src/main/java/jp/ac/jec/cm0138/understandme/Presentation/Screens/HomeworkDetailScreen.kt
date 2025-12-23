package jp.ac.jec.cm0138.understandme.Presentation.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import jp.ac.jec.cm0138.understandme.Entity.HomeworkState
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.HeaderAndBackButton
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.LottieView
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.HomeworkDetailScreenViewModel
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import kotlinx.coroutines.launch

@Composable
fun HomeworkDetailScreen(
    homeworkID: String,
    viewModel: HomeworkDetailScreenViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    LaunchedEffect(Unit) {
        viewModel.loadData(homeworkID)
    }


    Scaffold(
        modifier = modifier, topBar = {
            HeaderAndBackButton(
                headerTitle = "課題詳細",
                onBackButtonClicked = { navController.popBackStack() })
        }) { innerPadding ->


        val homework = viewModel.homework
        val className = viewModel.className

        if (homework == null || className == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GeneralInfoOfHomework(
                title = homework.title,
                description = homework.description ?: "説明なし",
                className = className,
                dueDate = homework.dueDateString ?: "締切なし",
                score = viewModel.result?.score
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp))
            Spacer(modifier = Modifier.height(16.dp))

            when (homework.submissionState) {
                HomeworkState.notAssigned -> {
                    GithubTextFieldAndButton(
                        homeworkLinkTxt = viewModel.projectLink,
                        onLinkChange = {
                            viewModel.projectLink = it
                        },
                        showInputError = viewModel.showErrorMessage,
                        inputErrorMessage = viewModel.errorMessage,
                        onSubmit = {
                            viewModel.uploadProject()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HomeworkState.generatingQuestions -> {
                    GeneratingUI(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                }

                HomeworkState.questionGenerated -> {
                    QuestionGeneratedUI(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        onAnswerBtnClick = {}
                    )
                }

                HomeworkState.completed -> {
                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .padding(horizontal = 10.dp),
                        onClick = {},
                        buttonText = "回答履歴を見る"
                    )
                }

                HomeworkState.failed -> {
                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .padding(horizontal = 10.dp),


                        onClick = { viewModel.regenerateQuestions(homeworkID = homeworkID) },
                        buttonText = "生成やり直す"
                    )

                    OutlinedButton(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .height(55.dp),

                        onClick = { viewModel.cancelSubmission(homeworkID) }

                    ) {
                        Text(
                            "提出を取り消す",
                            style = CustomTypography.header.copy(
                                fontSize = 15.sp,
                                color = Color.Red
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonText: String
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(30.dp)),
        color = MyAppTheme.colors.accent
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = buttonText,
                modifier = Modifier,
                style = CustomTypography.header.copy(
                    color = Color.White,
                    fontSize = 15.sp
                )
            )
        }
    }
}


@Composable
fun QuestionGeneratedUI(
    onAnswerBtnClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        onClick = onAnswerBtnClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(MyAppTheme.colors.accent),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LottieView(
                R.raw.ai,
                modifier = Modifier.width(50.dp)
            )

            Text(
                text = "クイズに回答",
                modifier = Modifier.padding(start = 8.dp),
                style = CustomTypography.header.copy(
                    color = Color.White,
                    fontSize = 15.sp
                )
            )
        }
    }
}


@Composable
fun GeneratingUI(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieView(
            id = R.raw.neko_thinking, modifier = Modifier.size(200.dp)
        )

        Text(
            "猫ちゃん考え中です。\n クイズが用意出来次第通知します。", textAlign = TextAlign.Center
        )
    }
}


@Composable
fun GithubTextFieldAndButton(
    homeworkLinkTxt: String,
    onLinkChange: (String) -> Unit,
    showInputError: Boolean,
    inputErrorMessage: String,
    onSubmit: suspend () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Title
        Text(
            text = "提出リンク (GitHub または Google Drive)",
            style = CustomTypography.header.copy(fontSize = 14.sp)
        )

        // TextField
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlinedTextField(
                value = homeworkLinkTxt,
                onValueChange = onLinkChange,
                placeholder = {
                    Text("例: https://github.com/your-username/your-repository")
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(200.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        // Info message
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MyAppTheme.colors.secondary,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = "Google Driveで提出する場合は、ファイルを圧縮し、\n" + "「リンクを知っている全員がアクセス可能」に設定してください。",
                style = MaterialTheme.typography.bodySmall,
                color = MyAppTheme.colors.secondary
            )
        }

        // Error message
        AnimatedVisibility(visible = showInputError) {
            Text(
                text = inputErrorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
            )
        }

        // Submit button
        Button(
            onClick = {
                scope.launch {
                    onSubmit()
                    // placeholder:
                    // loadInfoOfHomework(homeworkId)
                }
            },
            enabled = homeworkLinkTxt.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(70.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (homeworkLinkTxt.isEmpty()) Color.Gray.copy(alpha = 0.4f)
                else MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "提出する", style = MaterialTheme.typography.titleMedium, color = Color.White
            )
        }
    }
}


@Composable
private fun GeneralInfoOfHomework(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    className: String,
    dueDate: String,
    score: Int?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // Title + Score
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = CustomTypography.title,
                modifier = Modifier.padding(bottom = 7.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Score badge
            if (score != null) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .size(48.dp)
                        .border(
                            width = 4.dp, brush = Brush.linearGradient(
                                colors = listOf(
                                    MyAppTheme.colors.accent, MyAppTheme.colors.secAccent
                                )
                            ), shape = CircleShape
                        )
                ) {
                    Text(
                        text = "$score 点", fontSize = 10.sp, fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Class info
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.School, contentDescription = null
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = className)
        }

        // Due date
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange, contentDescription = null
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "締切：$dueDate")
        }

        // Description
        Text(
            text = description, style = MaterialTheme.typography.bodyMedium
        )
    }
}
