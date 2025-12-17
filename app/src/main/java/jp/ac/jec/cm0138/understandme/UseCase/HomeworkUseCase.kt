package jp.ac.jec.cm0138.understandme.UseCase

import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus
import jp.ac.jec.cm0138.understandme.Repository.Abstract.HomeworkRepository
import javax.inject.Inject

class HomeworkUseCase @Inject constructor(
    private val homeworkRepository: HomeworkRepository
) {
    suspend fun fetchHomeworks(studentID: String, homeworkID: String): HomeworkWithStatus {
        return homeworkRepository.fetchHomework(homeworkID, studentID)
    }

    suspend fun fetchAllHomeworks(studentID: String): List<HomeworkWithStatus> {
        return homeworkRepository.fetchHomeworks(studentID)
    }

    suspend fun fetchHomeworksForClass(classID: String, studentID: String): List<HomeworkWithStatus> {
        return homeworkRepository.fetchHomeworksFromClass(classID, studentID)
    }

    suspend fun retryQuestionGeneration(homeworkID: String, studentID: String) {
        homeworkRepository.retryQuestionGeneration(homeworkID, studentID)
    }


    suspend fun cancelHomeworkSubmission(homeworkID: String, studentID: String) {
        homeworkRepository.cancelHomeworkSubmission(homeworkID, studentID)
    }

}