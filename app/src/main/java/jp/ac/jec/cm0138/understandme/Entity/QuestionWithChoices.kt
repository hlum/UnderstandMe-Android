package jp.ac.jec.cm0138.understandme.Entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionWithChoices(
    @SerialName("question_id")
    val id: String,
    @SerialName("job_id")
    val jobID: String,
    @SerialName("project_id")
    val projectID: String,
    @SerialName("homework_id")
    val homeworkID: String,
    @SerialName("user_id")
    val userID: String,
    @SerialName("question_text")
    val questionText: String,
    @SerialName("choices")
    val choices: List<Choice>,
    @SerialName("created_at")
    val createdAt: String
) {
    companion object{
        fun getDummy(): QuestionWithChoices {
            return QuestionWithChoices(
                id = "question-1234",
                jobID = "job-5678",
                projectID = "project-91011",
                homeworkID = "homework-1213",
                userID = "user-1415",
                questionText = "What is the capital of France?",
                choices = listOf(
                    Choice(
                        id = "choice-1",
                        choiceText = "Berlin",
                        isCorrect = false
                    ),
                    Choice(
                        id = "choice-2",
                        choiceText = "Madrid",
                        isCorrect = false
                    ),
                    Choice(
                        id = "choice-3",
                        choiceText = "Paris",
                        isCorrect = true
                    ),
                    Choice(
                        id = "choice-4",
                        choiceText = "Rome",
                        isCorrect = false
                    )
                ),
                createdAt = "2024-01-01T12:00:00Z"
            )
        }
    }
}


@Serializable
data class Choice(
    @SerialName("choice_id")
    val id: String,
    @SerialName("choice_text")
    val choiceText: String,
    @SerialName("is_correct")
    val isCorrect: Boolean
) {
    companion object {
        fun getDummy(): Choice {
            return Choice(
                id = "choice-1234",
                choiceText = "Dummy Choice Text",
                isCorrect = false
            )
        }
    }
}
