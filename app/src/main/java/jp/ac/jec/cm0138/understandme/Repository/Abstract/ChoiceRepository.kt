package jp.ac.jec.cm0138.understandme.Repository.Abstract

import jp.ac.jec.cm0138.understandme.Entity.Choice

interface ChoiceRepository {
    suspend fun fetchCorrectChoice(questionID: String, homeworkID: String): Choice
}
