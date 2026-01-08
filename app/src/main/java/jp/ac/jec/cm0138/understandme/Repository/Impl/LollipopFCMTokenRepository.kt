package jp.ac.jec.cm0138.understandme.Repository.Impl

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.FCMTokenRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.DeleteFCMTokenRequest
import jp.ac.jec.cm0138.understandme.Retrofit.Services.FCMTokenAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.UpdateFCMTokenRequest

class LollipopFCMTokenRepository @Inject constructor(
    private val fcmTokenAPIService: FCMTokenAPIService
): FCMTokenRepository {
    override suspend fun saveOrUpdateToken(
        userID: String,
        deviceID: String,
        deviceType: String,
        fcmToken: String
    ) {
        val response = fcmTokenAPIService.updateFCMToken(
            request = UpdateFCMTokenRequest(
                user_id = userID,
                device_id = deviceID,
                fcm_token = fcmToken,
                device_type = deviceType,
            )
        )

        LollipopAPIHelper.handleAPIResponse(response)
    }

    override suspend fun deleteFcmToken(userID: String, deviceID: String) {
        val response = fcmTokenAPIService.deleteFCMToken(
            request = DeleteFCMTokenRequest(
                user_id = userID,
                device_id = deviceID
            )
        )
        LollipopAPIHelper.handleAPIResponse(response)
    }
}