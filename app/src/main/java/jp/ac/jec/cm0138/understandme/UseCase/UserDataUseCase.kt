package jp.ac.jec.cm0138.understandme.UseCase

import android.util.Log
import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.UserData
import jp.ac.jec.cm0138.understandme.Repository.Abstract.UserDataRepository

class UserDataUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {

    val TAG = "UserDataUseCase"

    suspend fun registerUserIfNotExists(userData: UserData) {
        val userInDB = try {
            userDataRepository.fetchUserData(userData.id)
        } catch (e: Exception) {
            null
        }

        if (userInDB == null) {
                Log.d(TAG, "registerUserIfNotExists: registering user")
                userDataRepository.saveUserData(userData)
        }
    }


    suspend fun updateFCMToken(userID: String, fcmToken: String) {
        // TODO: Impl deviceManager and send fetch FCM token
    }


    suspend fun fetchUserData(userID: String): UserData {
        // TODO: add error handling
        return userDataRepository.fetchUserData(userID)
    }
}