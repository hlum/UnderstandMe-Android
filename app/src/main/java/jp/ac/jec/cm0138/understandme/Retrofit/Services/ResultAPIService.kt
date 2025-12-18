package jp.ac.jec.cm0138.understandme.Retrofit.Services

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import jp.ac.jec.cm0138.understandme.Entity.ResultData
import retrofit2.http.GET
import retrofit2.http.Query

interface ResultAPIService {

    @GET("result/get_result.php")
    suspend fun fetchResults(
        @Query("user_id") userID: String,
        @Query("year") year: Int
    ): APIResponse<List<ResultData>>

    @GET("result/get_result_userID_homeworkID.php")
    suspend fun fetchResult(
        @Query("user_id") userID: String,
        @Query("homework_id") homeworkID: String
    ): APIResponse<List<ResultData>>
}
