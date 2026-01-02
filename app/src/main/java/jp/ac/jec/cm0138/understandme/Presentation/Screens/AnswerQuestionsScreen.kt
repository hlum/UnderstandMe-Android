package jp.ac.jec.cm0138.understandme.Presentation.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm0138.understandme.Entity.Choice
import jp.ac.jec.cm0138.understandme.Entity.QuestionWithChoices
import jp.ac.jec.cm0138.understandme.customTheme.CustomTypography
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import jp.ac.jec.cm0138.understandme.customTheme.customPrimaryButtonColors

@Composable
fun AnswerQuestionsScreen(modifier: Modifier = Modifier) {
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        QuestionCard(
            questionWithChoices = QuestionWithChoices.getDummy(),
            onSubmit = { submitted = true },
            onNextQuestionClick = {},
            submitted = submitted,
            modifier = Modifier.padding(10.dp)
        )
    }
}


@Composable
fun QuestionCard(
    questionWithChoices: QuestionWithChoices,
    submitted: Boolean,
    onSubmit: (String) -> Unit,
    onNextQuestionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedChoiceID by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(12.dp))
                .background(MyAppTheme.colors.background, RoundedCornerShape(12.dp))
                .padding(10.dp)
        ) {
            Text(
                "残り時間: 10s",
                modifier = Modifier,
                style = CustomTypography.body.copy(
                    color = Color.Red
                )
            )

            Text(
                questionWithChoices.questionText,
                style = CustomTypography.titleMedium.copy(fontSize = 20.sp),
                modifier = Modifier.padding(vertical = 10.dp)
            )



            questionWithChoices.choices.forEach {
                ChoiceButton(
                    choice = it,
                    isSelected = selectedChoiceID == it.id,
                    submitted = submitted,
                    onSelect = { selectedChoiceID = it.id },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }



            Button(
                onClick = {
                    if(selectedChoiceID != null && !submitted) {
                        onSubmit(selectedChoiceID!!)
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
                val buttonText = if(submitted) "次の質問へ" else "回答を送信"
                Text(
                    text = buttonText,
                    style = CustomTypography.header.copy(fontSize = 18 .sp),
                    color = Color.White
                )
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
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
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



@Preview(showBackground = true, showSystemUi = true, name = "AnswerQuestionsScreen Preview")
@Composable
fun AnswerQuestionsScreenPreview() {
    Scaffold { innerPadding ->
        AnswerQuestionsScreen(modifier = Modifier.padding(paddingValues = innerPadding))
    }
}