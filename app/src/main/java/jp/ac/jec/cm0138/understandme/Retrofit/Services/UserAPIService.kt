package jp.ac.jec.cm0138.understandme.Retrofit.Services

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import jp.ac.jec.cm0138.understandme.Entity.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserAPIService {

    @POST("user/register.php")
    suspend fun registerUser(
        @Body user: UserData
    ): Response<APIResponse<Unit>>

    @GET("user/get_user.php")
    suspend fun getUser(
        @Query("id") userID: String
    ): Response<APIResponse<List<UserData>>>


    @FormUrlEncoded
    @POST("user/update_fcm_token.php")
    suspend fun updateFCM(
        @Field("user_id") userId: String,
        @Field("fcm_token") fcmToken: String
    ): Response<APIResponse<Unit>>


}