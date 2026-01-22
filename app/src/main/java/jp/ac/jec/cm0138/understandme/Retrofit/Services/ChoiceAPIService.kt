package jp.ac.jec.cm0138.understandme.Retrofit.Services

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import jp.ac.jec.cm0138.understandme.Entity.Choice
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChoiceAPIService {
    @GET("choices/get_correct_choice.php")
    suspend fun fetchCorrectChoice(
        @Query("question_id") questionID: String,
        @Query("homework_id") homeworkID: String
    ): Response<APIResponse<List<Choice>>>
}
