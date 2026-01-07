package jp.ac.jec.cm0138.understandme.Repository.Impl

import jp.ac.jec.cm0138.understandme.Entity.QuestionWithChoices
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.QuestionWithChoicesRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.QuestionWithChoicesAPIService
import javax.inject.Inject

class LollipopQuestionWithChoicesRepository @Inject constructor(
    private val questionWithChoicesAPIService: QuestionWithChoicesAPIService
): QuestionWithChoicesRepository {
    override suspend fun fetchQuestionWithChoices(
        homeworkID: String,
        userID: String
    ): List<QuestionWithChoices> {
        val response = questionWithChoicesAPIService.fetchQuestionsWithChoices(
            userID = userID,
            homeworkID = homeworkID
        )

        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)

        return apiResponse.data ?: throw IllegalStateException("Questions with choices not found")
    }
}