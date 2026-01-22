package jp.ac.jec.cm0138.understandme.UseCase

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.Choice
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ChoiceRepository

class ChoiceUseCase @Inject constructor(
    private val choiceRepository: ChoiceRepository
) {
    suspend fun fetchCorrectChoice(questionID: String, homeworkID: String): Choice {
        return choiceRepository.fetchCorrectChoice(questionID, homeworkID)
    }
}
