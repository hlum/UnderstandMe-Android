package jp.ac.jec.cm0138.understandme.Repository.Impl

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.UserData
import jp.ac.jec.cm0138.understandme.Repository.Abstract.UserDataRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.UserAPIService


class LollipopUserDataRepository @Inject constructor (
    private val api: UserAPIService
) : UserDataRepository {

    override suspend fun saveUserData(userData: UserData) {
        val response = api.registerUser(userData)

        if (response.status != "success") {
            throw Exception("Error: ${response.message}")
        }
    }

    override suspend fun fetchUserData(userID: String): UserData {
        val response = api.getUser(userID)

        if (response.status != "success") {
            throw Exception("Error: ${response.message}")
        }

        val list = response.data ?: throw Exception("No data")

        return list.firstOrNull() ?: throw Exception("User not found")
    }

    override suspend fun updateFCMToken(userID: String, fcmToken: String) {
        val response = api.updateFCM(userID, fcmToken)

        if (response.status != "success") {
            throw Exception("Error: ${response.message}")
        }
    }
}
