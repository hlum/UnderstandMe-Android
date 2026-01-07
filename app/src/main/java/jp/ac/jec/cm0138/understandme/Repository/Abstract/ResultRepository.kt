package jp.ac.jec.cm0138.understandme.Repository.Abstract

import jp.ac.jec.cm0138.understandme.Entity.ResultData

interface ResultRepository {
    suspend fun fetchResults(userID: String, year: Int): List<ResultData>
    suspend fun fetchResult(userID: String, homeworkID: String): ResultData
}