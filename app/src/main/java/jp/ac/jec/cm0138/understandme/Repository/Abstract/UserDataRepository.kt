package jp.ac.jec.cm0138.understandme.Repository.Abstract

import jp.ac.jec.cm0138.understandme.Entity.UserData

interface UserDataRepository {

    suspend fun saveUserData(userData: UserData)

    suspend fun fetchUserData(userID: String): UserData

    suspend fun updateFCMToken(userID: String, fcmToken: String)

    suspend fun deleteUserData(userID: String)
}

