package jp.ac.jec.cm0138.understandme.Retrofit.Services

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import jp.ac.jec.cm0138.understandme.Entity.QuestionWithChoices
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionWithChoicesAPIService {

    @GET("questions_choices/get_questions_choices.php")
    suspend fun fetchQuestionsWithChoices(
        @Query("homework_id") homeworkID: String,
        @Query("user_id") userID: String
    ): APIResponse<List<QuestionWithChoices>>

}