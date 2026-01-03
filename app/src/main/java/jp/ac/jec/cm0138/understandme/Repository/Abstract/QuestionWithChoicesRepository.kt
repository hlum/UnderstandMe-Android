package jp.ac.jec.cm0138.understandme.Repository.Abstract

import jp.ac.jec.cm0138.understandme.Entity.QuestionWithChoices

interface QuestionWithChoicesRepository {
    suspend fun fetchQuestionWithChoices(homeworkID: String, userID: String): List<QuestionWithChoices>
}