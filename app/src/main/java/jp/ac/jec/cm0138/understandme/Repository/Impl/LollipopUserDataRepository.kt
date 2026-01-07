package jp.ac.jec.cm0138.understandme.Repository.Impl

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.UserData
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.UserDataRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.UserAPIService


class LollipopUserDataRepository @Inject constructor (
    private val api: UserAPIService
) : UserDataRepository {

    override suspend fun saveUserData(userData: UserData) {
        val response = api.registerUser(userData)

        LollipopAPIHelper.handleAPIResponse(response)
    }

    override suspend fun fetchUserData(userID: String): UserData {
        val response = api.getUser(userID)

        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)

        return apiResponse.data?.firstOrNull() ?: throw Exception("User not found")
    }

    override suspend fun updateFCMToken(userID: String, fcmToken: String) {
        val response = api.updateFCM(userID, fcmToken)

        LollipopAPIHelper.handleAPIResponse(response)
    }
}
