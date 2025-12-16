package jp.ac.jec.cm0138.understandme.Entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Class (
    val id: String,
    val name: String,
    @SerialName("teacher_id") val teacherID: String,
    @SerialName("teacher_name") val teacherName: String,
    @SerialName("admission_year") val admissionYear: Int,
    @SerialName("major_code") val majorCode: String,
    @SerialName("class_code") val classCode: String?
) {
    companion object {
        fun getDummy(): Class {
            return Class(
                id = java.util.UUID.randomUUID().toString(),
                name = "テストクラス",
                teacherID = java.util.UUID.randomUUID().toString(),
                teacherName = "テスト先生",
                admissionYear = 38,
                majorCode = "cm",
                classCode = "24cm0138"
            )
        }
    }
}