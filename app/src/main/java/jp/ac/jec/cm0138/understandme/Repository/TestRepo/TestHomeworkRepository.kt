package jp.ac.jec.cm0138.understandme.Repository.TestRepo

import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus
import jp.ac.jec.cm0138.understandme.Repository.Abstract.HomeworkRepository

class TestHomeworkRepository : HomeworkRepository {
    val homeworkLists = listOf(
        HomeworkWithStatus.getDummy(),
        HomeworkWithStatus.getDummy(),
        HomeworkWithStatus.getDummy()
    )

    override suspend fun fetchHomeworks(studentID: String): List<HomeworkWithStatus> {
        return homeworkLists
    }

    override suspend fun fetchHomeworksFromClass(
        classID: String,
        studentID: String
    ): List<HomeworkWithStatus> {
        return homeworkLists
    }

    override suspend fun fetchHomework(
        id: String,
        studentID: String
    ): HomeworkWithStatus {
        return HomeworkWithStatus.getDummy()
    }

    override suspend fun retryQuestionGeneration(
        homeworkID: String,
        studentID: String
    ) {
    }

    override suspend fun cancelHomeworkSubmission(
        homeworkID: String,
        studentID: String
    ) {
    }
}