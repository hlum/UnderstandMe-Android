package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.QuestionWithChoices
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.UseCase.AnswerUseCase
import jp.ac.jec.cm0138.understandme.UseCase.ChoiceUseCase
import jp.ac.jec.cm0138.understandme.UseCase.QuestionWithChoicesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class UserAnswerAndQuestionID(
    val questionsAndChoices: QuestionWithChoices,
    val userChoiceID: String?
)

@HiltViewModel
class ResultConfirmationScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questionWithChoicesUseCase: QuestionWithChoicesUseCase,
    private val answerUseCase: AnswerUseCase,
    private val choiceUseCase: ChoiceUseCase
) : ViewModel() {
    val TAG = "ResultConfirmationVM"

    var questionsWithChoices by mutableStateOf<List<QuestionWithChoices>>(emptyList())
        private set

    var userAnswers by mutableStateOf<List<UserAnswerAndQuestionID>>(emptyList())
        private set

    var correctChoiceIDs by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    var isLoading by mutableStateOf(false)
        private set


    fun loadAnswers(homeworkID: String) {
        viewModelScope.launch {
            try {
                isLoading = true

                loadQuestions(homeworkID)

                val userAnswerAndQuestionID = withContext(Dispatchers.IO) {
                    val userID = authRepository.getCurrentUser().uid
                    val answers = answerUseCase.fetchAnswers(
                        homeworkID = homeworkID,
                        userID = userID
                    )

                    questionsWithChoices.map { question ->
                        val matchedAnswer = answers.firstOrNull { answer ->
                            question.id == answer.questionID
                        }

                        UserAnswerAndQuestionID(
                            questionsAndChoices = question,
                            userChoiceID = matchedAnswer?.selectedChoiceID // null if not answered
                        )
                    }
                }

                userAnswers = userAnswerAndQuestionID

                // Load correct choices for all questions
                loadCorrectChoices(homeworkID)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading answers: ${e.message}")
            } finally {
                isLoading = false
            }

        }
    }

    private suspend fun loadCorrectChoices(homeworkID: String) {
        try {
            val correctChoices = mutableMapOf<String, String>()

            withContext(Dispatchers.IO) {
                questionsWithChoices.forEach { question ->
                    try {
                        val choice = choiceUseCase.fetchCorrectChoice(
                            questionID = question.id,
                            homeworkID = homeworkID
                        )
                        correctChoices[question.id] = choice.id
                    } catch (e: Exception) {
                        Log.e(TAG, "Error loading correct choice for question ${question.id}: ${e.message}")
                    }
                }
            }

            correctChoiceIDs = correctChoices
        } catch (e: Exception) {
            Log.e(TAG, "Error loading correct choices: ${e.message}")
        }
    }

    private suspend fun loadQuestions(homeworkID: String) {
        try {
            val questionWithChoicesList = withContext(Dispatchers.IO) {
                val currentUser = authRepository.getCurrentUser()
                questionWithChoicesUseCase.fetchQuestionWithChoices(
                    userID = currentUser.uid,
                    homeworkID = homeworkID
                )
            }

            questionsWithChoices = questionWithChoicesList

        } catch (e: Exception) {
            // TODO: Handle error appropriately
            Log.e(TAG, "Error loading questions: ${e.message}")
        }
    }
}

