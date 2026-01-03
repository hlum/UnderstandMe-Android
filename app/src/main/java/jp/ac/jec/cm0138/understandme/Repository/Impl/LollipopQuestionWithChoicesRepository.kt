package jp.ac.jec.cm0138.understandme.Repository.Impl

import jp.ac.jec.cm0138.understandme.Entity.QuestionWithChoices
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

        if (response.status != "success") {
            throw Exception("Error: ${response.message}")
        }

        val list = response.data ?: throw Exception("No data")

        return list
    }
}