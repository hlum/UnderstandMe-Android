package jp.ac.jec.cm0138.understandme.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Answer(
    val id: String = UUID.randomUUID().toString(),

    @SerialName("question_id")
    val questionID: String,

    @SerialName("user_id")
    val userID: String,

    @SerialName("selected_choice_id")
    val selectedChoiceID: String? = null
) {
    companion object {
        fun getDummy(): Answer {
            return Answer(
                questionID = "testquestionID",
                userID = "testUserID",
                selectedChoiceID = "testSleectechoiceid"
            )
        }
    }
}

@Serializable
data class PostAnswerResponse(
    @SerialName("correct_choice_id")
    val correctChoiceID: String?
)
