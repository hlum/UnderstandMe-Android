package jp.ac.jec.cm0138.understandme.Entity

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class ResultData(
    val id: String,
    @SerialName("user_id") val userID: String,
    @SerialName("total_questions") val totalQuestions: Int,
    @SerialName("homework_id") val homeworkID: String,
    @SerialName("correct_answers") val correctAnswers: Int,
    val score: Int,
    @SerialName("evaluated_at") val evaluatedAt: String
) {
    companion object {
        // Function to create dummy data
        fun getDummy(): ResultData {
            return ResultData(
                id = UUID.randomUUID().toString(),
                userID = UUID.randomUUID().toString(),
                homeworkID = UUID.randomUUID().toString(),
                totalQuestions = 10,
                correctAnswers = 8,
                score = 80,
                evaluatedAt = "2024-01-01 12:00:00"
            )
        }
    }
}
