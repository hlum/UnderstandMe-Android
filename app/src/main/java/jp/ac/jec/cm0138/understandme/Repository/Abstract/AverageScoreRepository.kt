package jp.ac.jec.cm0138.understandme.Repository.Abstract

import jp.ac.jec.cm0138.understandme.Entity.AverageScorePerClass

interface AverageScoreRepository {
    suspend fun fetchAverageScores(
        userID: String,
    ): List<AverageScorePerClass>
}