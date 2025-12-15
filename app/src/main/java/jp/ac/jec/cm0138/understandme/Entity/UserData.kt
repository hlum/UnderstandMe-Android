package jp.ac.jec.cm0138.understandme.Entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val id: String,
    val email: String,
    val name: String,
    @SerialName("fcm_token") val fcmToken: String? = null,
    @SerialName("student_code") val studentCode: String? = null,
    @SerialName("major_code") val majorCode: String? = null,
    @SerialName("admission_year") val admissionYear: Int? = null,
    @SerialName("photo_url") val photoURL: String? = null
) {
    val displayName: String
        get() = if (studentCode == "99zz") "Guest" else studentCode ?: "Unknown"

    companion object {
        fun getDummy(): UserData {
            return UserData(
                id = java.util.UUID.randomUUID().toString(),
                email = "24cm0138@jec.ac.jp",
                name = "テストユーザー",
                fcmToken = null,
                studentCode = "24cm0138",
                majorCode = "24",
                admissionYear = 24,
                photoURL = "cm"
            )
        }
    }
}
