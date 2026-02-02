package jp.ac.jec.cm0138.understandme.UseCase

import android.bluetooth.BluetoothClass
import android.content.Context
import android.util.Log
import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.UserData
import jp.ac.jec.cm0138.understandme.Helper.DeviceManager
import jp.ac.jec.cm0138.understandme.Repository.Abstract.FCMTokenRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.UserDataRepository

class UserDataUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val fcmTokenRepository: FCMTokenRepository
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





    suspend fun fetchUserData(userID: String): UserData {
        return userDataRepository.fetchUserData(userID)
    }

    suspend fun updateFCMToken(context: Context, userID: String, fcmToken: String) {
        val deviceID = DeviceManager.getDeviceId(context)
        val deviceType = DeviceManager.getDeviceType()

        fcmTokenRepository.saveOrUpdateToken(
            userID = userID,
            fcmToken = fcmToken,
            deviceID = deviceID,
            deviceType = deviceType
        )
    }


    suspend fun deleteFCMToken(context: Context, userID: String) {
        val deviceID = DeviceManager.getDeviceId(context)
        fcmTokenRepository.deleteFcmToken(userID = userID, deviceID = deviceID)
    }

    suspend fun deleteUserData(userID: String) {
        userDataRepository.deleteUserData(userID)
    }
}