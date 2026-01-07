package jp.ac.jec.cm0138.understandme.Repository.Impl

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ProjectRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ProjectAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.UploadProjectRequest

class LollipopProjectRepository @Inject constructor(
    private val projectAPIService: ProjectAPIService
): ProjectRepository {
    override suspend fun uploadProject(
        userID: String,
        homeworkID: String,
        projectLink: String
    ) {
        val request = UploadProjectRequest(
            user_id = userID,
            homework_id = homeworkID,
            github_file_link = projectLink
        )

        val response = projectAPIService.uploadProject(request)
        LollipopAPIHelper.handleAPIResponse(response)
    }

}