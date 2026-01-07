package jp.ac.jec.cm0138.understandme.Retrofit.Services

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import jp.ac.jec.cm0138.understandme.Entity.Answer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@Serializable
data class SubmitAnswerRequest(
    @SerialName("question_id") val questionID: String,
    @SerialName("homework_id") val homeworkID: String,
    @SerialName("user_id") val userID: String,
    @SerialName("selected_choice_id") val selectedChoiceID: String?,
    @SerialName("total_questions") val totalQuestions: Int
)

interface AnswerAPIService {
    @POST("answer/add_answer.php")
    suspend fun submitAnswer(
        @Body request: SubmitAnswerRequest
    ): Response<APIResponse<Unit>>


    @GET("answer/get_answers_with_homeworkID.php")
    suspend fun fetchAnswers(
        @Query("homework_id") homeworkID: String,
        @Query("user_id") userID: String
    ): Response<APIResponse<List<Answer>>>
}