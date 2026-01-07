package jp.ac.jec.cm0138.understandme.UseCase

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Repository.Abstract.QuestionWithChoicesRepository

class QuestionWithChoicesUseCase @Inject constructor(
    private val questionWithChoicesRepository: QuestionWithChoicesRepository
) {

    val TAG = "QuestionWithChoicesUseCase"

    suspend fun fetchQuestionWithChoices(homeworkID: String, userID: String) =
        questionWithChoicesRepository.fetchQuestionWithChoices(homeworkID, userID)
}