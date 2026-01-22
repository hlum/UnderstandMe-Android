package jp.ac.jec.cm0138.understandme.UseCase

import android.util.Log
import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AnswerRepository

class AnswerUseCase @Inject constructor(
    private val answerRepository: AnswerRepository
) {
    private val TAG = "AnswerUseCase"

    suspend fun submitAnswer(
        questionID: String,
        homeworkID: String,
        userID: String,
        selectedChoiceID: String?,
        totalQuestion: Int
    ): String? {
            return answerRepository.postAnswers(
                questionID = questionID,
                homeworkID = homeworkID,
                userID = userID,
                selectedChoiceID = selectedChoiceID,
                totalQuestion = totalQuestion
            )
    }

    suspend fun fetchAnswers(
        homeworkID: String,
        userID: String
    ) = answerRepository.fetchAnswers(
        homeworkID = homeworkID,
        userID = userID
    )
}