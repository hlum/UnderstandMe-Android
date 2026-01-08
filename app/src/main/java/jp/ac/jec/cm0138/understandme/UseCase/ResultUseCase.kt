package jp.ac.jec.cm0138.understandme.UseCase

import jp.ac.jec.cm0138.understandme.Repository.Abstract.ResultRepository
import javax.inject.Inject

class ResultUseCase @Inject constructor(
    private val resultRepository: ResultRepository
) {

    suspend fun fetchResults(
        userID: String,
    ) = resultRepository.fetchResults(userID)

    suspend fun fetchResult(
        userID: String,
        homeworkID: String
    ) = resultRepository.fetchResult(userID, homeworkID)
}