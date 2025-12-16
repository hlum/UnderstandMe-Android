package jp.ac.jec.cm0138.understandme.Repository.TestRepo

import jp.ac.jec.cm0138.understandme.Entity.UserData
import jp.ac.jec.cm0138.understandme.Repository.Abstract.UserDataRepository

class TestUserDataRepository: UserDataRepository {
    override suspend fun saveUserData(userData: UserData) {
        return
    }

    override suspend fun fetchUserData(userID: String): UserData {
        return UserData.getDummy()
    }

    override suspend fun updateFCMToken(userID: String, fcmToken: String) {
    }
}