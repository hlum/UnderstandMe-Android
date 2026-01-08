package jp.ac.jec.cm0138.understandme.Entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AverageScorePerClass(
    val id: String = UUID.randomUUID().toString(),
    @SerialName("class_name") val className: String,
    @SerialName("average_score") val averageScore: Int,
    @SerialName("finished_homework_count") val finishedHomeworkCount: Int,
    @SerialName("total_homework_count") val totalHomeworkCount: Int
)