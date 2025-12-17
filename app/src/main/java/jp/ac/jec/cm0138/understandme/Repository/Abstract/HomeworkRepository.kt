package jp.ac.jec.cm0138.understandme.Repository.Abstract

import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus

interface HomeworkRepository {
    suspend fun fetchHomeworks(studentID: String): List<HomeworkWithStatus>
    suspend fun fetchHomeworksFromClass(classID: String, studentID: String): List<HomeworkWithStatus>
    suspend fun fetchHomework(id: String, studentID: String): HomeworkWithStatus
    suspend fun retryQuestionGeneration(homeworkID: String, studentID: String)
    suspend fun cancelHomeworkSubmission(homeworkID: String, studentID: String)
}