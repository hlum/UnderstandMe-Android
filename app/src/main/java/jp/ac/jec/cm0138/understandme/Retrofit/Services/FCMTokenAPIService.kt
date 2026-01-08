package jp.ac.jec.cm0138.understandme.Retrofit.Services

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.POST

@Serializable
data class UpdateFCMTokenRequest(
    val user_id: String,
    val device_id: String,
    val device_type: String,
    val fcm_token: String,
)


@Serializable
data class DeleteFCMTokenRequest(
    val user_id: String,
    val device_id: String,
)

interface FCMTokenAPIService {
    @POST("user/update_fcm_token.php")
    suspend fun updateFCMToken(
        @Body request: UpdateFCMTokenRequest
    ): Response<APIResponse<Unit>>

    @HTTP(method = "DELETE", path ="user/delete_fcm_token.php", hasBody = true)
    suspend fun deleteFCMToken(
        @Body request: DeleteFCMTokenRequest
    ): Response<APIResponse<Unit>>

}