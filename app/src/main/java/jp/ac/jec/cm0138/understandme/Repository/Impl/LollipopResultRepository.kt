package jp.ac.jec.cm0138.understandme.LollipopResultRepository.Impl

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.ResultData
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ResultRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ResultAPIService

class LollipopResultRepository @Inject constructor(
    private val resultAPIService: ResultAPIService
): ResultRepository {
    override suspend fun fetchResults(
        userID: String,
        year: Int
    ): List<ResultData> {
        val response = resultAPIService.fetchResults(userID, year)
        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)

        return apiResponse.data ?: throw IllegalStateException("No result data")
    }

    override suspend fun fetchResult(
        userID: String,
        homeworkID: String
    ): ResultData {
        val response = resultAPIService.fetchResult(userID, homeworkID)
        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)

        return apiResponse.data?.firstOrNull()
            ?: throw IllegalStateException("Result data not found")
    }
}