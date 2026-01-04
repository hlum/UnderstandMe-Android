package jp.ac.jec.cm0138.understandme.Repository.TestRepo

import jp.ac.jec.cm0138.understandme.Entity.QuestionWithChoices
import jp.ac.jec.cm0138.understandme.Repository.Abstract.QuestionWithChoicesRepository

class TestQuestionWithChoicesRepository: QuestionWithChoicesRepository {
    override suspend fun fetchQuestionWithChoices(
        homeworkID: String,
        userID: String
    ): List<QuestionWithChoices> {
        return listOf(
            QuestionWithChoices.getDummy(),
            QuestionWithChoices.getDummy(),
            QuestionWithChoices.getDummy()
        )
    }
}