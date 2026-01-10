package jp.ac.jec.cm0138.understandme.Repository.Abstract

interface FCMTokenRepository {
    suspend fun saveOrUpdateToken(
        userID: String,
        deviceID: String,
        deviceType: String,
        fcmToken: String
    )

    suspend fun deleteFcmToken(
        userID: String,
        deviceID: String
    )
}