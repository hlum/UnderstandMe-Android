package jp.ac.jec.cm0138.understandme.Entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Serializable
data class HomeworkWithStatus(
    val id: String,
    val title: String,
    val description: String? = null,

    @SerialName("due_date")
    val dueDateString: String? = null,

    @SerialName("class_id")
    val classID: String,

    @SerialName("github_file_link")
    val githubURL: String? = null,

    @SerialName("submission_state")
    val submissionState: HomeworkState,

    @SerialName("created_at")
    val createdAtString: String
) {

    /** yyyy-MM-dd */
    val dueDate: LocalDate?
        get() = dueDateString?.let {
            runCatching {
                LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }.getOrNull()
        }

    /** yyyy-MM-dd HH:mm:ss */
    val createdAt: LocalDateTime
        get() = runCatching {
            LocalDateTime.parse(createdAtString, dateTimeFormatter)
        }.getOrElse {
            LocalDateTime.MIN
        }

    companion object {
        private val dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        fun getDummy(
            submissionState: HomeworkState =
                HomeworkState.entries.randomOrNull() ?: HomeworkState.COMPLETED
        ): HomeworkWithStatus {
            return HomeworkWithStatus(
                id = UUID.randomUUID().toString(),
                title = "Test Dummy",
                description = "tasfjaosifjapiwnvjasvdsvasvlkma;slvmoas a",
                dueDateString = "2025-01-02",
                classID = UUID.randomUUID().toString(),
                githubURL = "https://example.com",
                submissionState = submissionState,
                createdAtString = "2025-12-01 10:00:00"
            )
        }
    }
}
