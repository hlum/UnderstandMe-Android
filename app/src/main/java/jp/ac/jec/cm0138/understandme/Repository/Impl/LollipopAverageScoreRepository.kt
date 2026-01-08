package jp.ac.jec.cm0138.understandme.Repository.Impl

import jp.ac.jec.cm0138.understandme.Entity.AverageScorePerClass
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AverageScoreRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.AverageScoreAPIService
import javax.inject.Inject

class LollipopAverageScoreRepository @Inject constructor(
    private val averageScoreAPIService: AverageScoreAPIService
): AverageScoreRepository {
    override suspend fun fetchAverageScores(userID: String): List<AverageScorePerClass> {
        val response = averageScoreAPIService.fetchAverageScores(userID)

        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)

        return apiResponse.data ?: throw Exception("No data received")
    }
}