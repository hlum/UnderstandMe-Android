package jp.ac.jec.cm0138.understandme.Repository.Impl

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.Choice
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ChoiceRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ChoiceAPIService

class LollipopChoiceRepository @Inject constructor(
    private val choiceAPIService: ChoiceAPIService
) : ChoiceRepository {
    override suspend fun fetchCorrectChoice(questionID: String, homeworkID: String): Choice {
        val response = choiceAPIService.fetchCorrectChoice(questionID, homeworkID)
        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)
        return apiResponse.data?.firstOrNull()
            ?: throw IllegalStateException("No correct choice found")
    }
}
