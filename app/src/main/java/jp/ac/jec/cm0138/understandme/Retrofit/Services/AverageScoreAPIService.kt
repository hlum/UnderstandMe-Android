package jp.ac.jec.cm0138.understandme.Retrofit.Services

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import jp.ac.jec.cm0138.understandme.Entity.AverageScorePerClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AverageScoreAPIService {
    @GET("average_score/get_average_score.php")
    suspend fun fetchAverageScores(
        @Query("student_id") studentID: String,
    ): Response<APIResponse<List<AverageScorePerClass>>>
}