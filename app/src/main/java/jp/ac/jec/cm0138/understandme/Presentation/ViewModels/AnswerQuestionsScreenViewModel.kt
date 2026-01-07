package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.jec.cm0138.understandme.Entity.QuestionWithChoices
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.UseCase.AnswerUseCase
import jp.ac.jec.cm0138.understandme.UseCase.QuestionWithChoicesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AnswerQuestionsScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questionWithChoicesUseCase: QuestionWithChoicesUseCase,
    private val answerUseCase: AnswerUseCase
) : ViewModel() {
    val TAG = "AnswerQuestionsVM"
    var questionsWithChoices by mutableStateOf<List<QuestionWithChoices>>(emptyList())
        private set

    var currentQuestionIndex by  mutableStateOf(0)
        private set

    var showSubmitErrorAlert by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set


    fun loadQuestions(homeworkID: String) {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true

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
            } finally {
                isLoading = false
            }
        }
    }


    fun postAnswer(
        questionID: String,
        homeworkID: String,
        selectedChoiceID: String?
    ) {
        if (isLoading) return

        viewModelScope.launch {
            val totalQuestionCount = questionsWithChoices.size
            isLoading = true

            val currentUser = authRepository.getCurrentUser()
            withContext(Dispatchers.Default) {
                try {
                    answerUseCase.submitAnswer(
                        questionID = questionID,
                        homeworkID = homeworkID,
                        userID = currentUser.uid,
                        selectedChoiceID = selectedChoiceID,
                        totalQuestion = totalQuestionCount
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Error submitting answer: ${e.message}")
                    showSubmitErrorAlert = true
                } finally {
                    isLoading = false
                }
            }
        }
    }


    fun goToNextQuestion(navController: NavController) {
        if (currentQuestionIndex >= questionsWithChoices.size - 1) {
            navController.popBackStack()
        } else {
            currentQuestionIndex++
        }
    }
}