package jp.ac.jec.cm0138.understandme.Retrofit.Services

import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

@Serializable
data class UploadProjectRequest(
    val user_id: String,
    val homework_id: String,
    val github_file_link: String
)

interface ProjectAPIService {
    @PATCH("project/add_project.php")
    suspend fun uploadProject(
        @Body request: UploadProjectRequest
    ): Response<APIResponse<Unit>>
}