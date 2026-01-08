package jp.ac.jec.cm0138.understandme.UseCase

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AverageScoreRepository

class AverageScoreUseCase @Inject constructor(
    private val averageScoreRepository: AverageScoreRepository
) {

    suspend fun fetchAverageScores(
        userID: String,
    ) = averageScoreRepository.fetchAverageScores(userID)
}