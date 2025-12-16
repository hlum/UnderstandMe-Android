package jp.ac.jec.cm0138.understandme.Retrofit.Services

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus
import retrofit2.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RetryJobRequest(
    @SerialName("homework_id")
    val homeworkId: String,

    @SerialName("user_id")
    val userId: String
)

@Serializable
data class CancelHomeworkRequest(
    @SerialName("user_id")
    val userId: String,

    @SerialName("homework_id")
    val homeworkId: String
)


interface HomeworkService {

    @GET("homework/get_homework_with_status.php")
    suspend fun getHomeworks(
        @Query("student_id") studentId: String
    ): APIResponse<List<HomeworkWithStatus>>

    @GET("homework/get_homework_with_status.php")
    suspend fun getHomeworksFromClass(
        @Query("class_id") classId: String,
        @Query("student_id") studentId: String
    ): APIResponse<List<HomeworkWithStatus>>

    @GET("homework/get_homework_with_status.php")
    suspend fun getHomework(
        @Query("id") homeworkId: String,
        @Query("student_id") studentId: String
    ): APIResponse<List<HomeworkWithStatus>>

    @PATCH("job/retry_job.php")
    suspend fun retryQuestionGeneration(
        @Body body: RetryJobRequest
    ): APIResponse<Unit>

    @HTTP(method = "DELETE", path = "homework/delete_submitted_homework.php", hasBody = true)
    suspend fun cancelHomeworkSubmission(
        @Body body: CancelHomeworkRequest
    ): APIResponse<Unit>
}
