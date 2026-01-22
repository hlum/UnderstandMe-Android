package jp.ac.jec.cm0138.understandme.Repository.Impl

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.Answer
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AnswerRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.AnswerAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.SubmitAnswerRequest

class LollipopAnswerRepository @Inject constructor(
    private val answerAPIService: AnswerAPIService
): AnswerRepository {

    override suspend fun postAnswers(
        questionID: String,
        homeworkID: String,
        userID: String,
        selectedChoiceID: String?,
        totalQuestion: Int
    ): String? {
        val request = SubmitAnswerRequest(
            questionID = questionID,
            homeworkID = homeworkID,
            userID = userID,
            selectedChoiceID = selectedChoiceID,
            totalQuestions = totalQuestion
        )

        val response = answerAPIService.submitAnswer(request)
        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)
        return apiResponse.data?.firstOrNull()?.correctChoiceID
    }

    override suspend fun fetchAnswers(
        homeworkID: String,
        userID: String
    ): List<Answer> {
        val response = answerAPIService.fetchAnswers(
            homeworkID = homeworkID,
            userID = userID
        )

        val apiResponse =  LollipopAPIHelper.handleAPIResponse(response)
        return apiResponse.data ?: throw IllegalStateException("No answer data found")
    }
}