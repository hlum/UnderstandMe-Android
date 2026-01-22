package jp.ac.jec.cm0138.understandme.Repository.Abstract

interface AnswerRepository {
    suspend fun postAnswers(
        questionID: String,
        homeworkID: String,
        userID: String,
        selectedChoiceID: String?,
        totalQuestion: Int
    ): String?

    suspend fun fetchAnswers(
        homeworkID: String,
        userID: String
    ): List<jp.ac.jec.cm0138.understandme.Entity.Answer>
}