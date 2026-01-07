package jp.ac.jec.cm0138.understandme.Entity

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
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
    val evaluatedAtDate: LocalDateTime
        get() = try {
            LocalDateTime.parse(
                evaluatedAt,
                DATE_FORMATTER
            )
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid date format: $evaluatedAt", e)
        }

    companion object {
        private val DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")




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
