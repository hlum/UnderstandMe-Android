package jp.ac.jec.cm0138.understandme.Retrofit.Services

import com.google.gson.annotations.SerializedName
import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import jp.ac.jec.cm0138.understandme.Entity.Class
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


data class AddOptionalClassRequest(
    @SerializedName("student_id")
    val studentId: String,

    @SerializedName("class_code")
    val classCode: String
)


interface ClassAPIService {

    @GET("class/get_class.php")
    suspend fun fetchClassWithClassCode(
        @Query("class_code") classCode: String
    ): APIResponse<List<Class>>

    @GET("class/get_class.php")
    suspend fun fetchClassWithID(
        @Query("id") id: String
    ): APIResponse<List<Class>>


    @GET("class/get_class.php")
    suspend fun fetchAllClass(
        @Query("student_id") userID: String
    ): APIResponse<List<Class>>

    @POST("class/enroll.php")
    suspend fun attendToOptionalClass(
        @Body request: AddOptionalClassRequest
        ): APIResponse<Unit>
}