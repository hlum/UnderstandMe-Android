package jp.ac.jec.cm0138.understandme.Repository.Impl
import android.util.Log
import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.APIResponse
import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.HomeworkRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.CancelHomeworkRequest
import jp.ac.jec.cm0138.understandme.Retrofit.Services.HomeworkAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.RetryJobRequest
import retrofit2.Response


class LollipopHomeworkRepository @Inject constructor(
    private val homeworkAPIService: HomeworkAPIService
): HomeworkRepository {

    override suspend fun fetchHomeworks(studentID: String): List<HomeworkWithStatus> {
        return fetchHomeworksInternal {
            homeworkAPIService.getHomeworks(studentID)
        }
    }

    override suspend fun fetchHomeworksFromClass(
        classID: String,
        studentID: String
    ): List<HomeworkWithStatus> {
        return fetchHomeworksInternal {
            homeworkAPIService.getHomeworksFromClass(classID, studentID)
        }
    }

    override suspend fun fetchHomework(
        id: String,
        studentID: String
    ): HomeworkWithStatus {
        val list = fetchHomeworksInternal {
            homeworkAPIService.getHomework(id, studentID)
        }

        return list.firstOrNull()
            ?: throw IllegalStateException("Bad server response")
    }

    private suspend fun fetchHomeworksInternal(
        request: suspend () -> Response<APIResponse<List<HomeworkWithStatus>>>
    ): List<HomeworkWithStatus> {

        val response = request()

        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)

        return apiResponse.data ?: emptyList()
    }

    override suspend fun retryQuestionGeneration(
        homeworkID: String,
        studentID: String
    ) {
        val response = homeworkAPIService.retryQuestionGeneration(
            RetryJobRequest(
                homeworkId = homeworkID,
                userId = studentID
            )
        )

        LollipopAPIHelper.handleAPIResponse(response)

        Log.i("LollipopRepo", "問題生成の再試行に成功: homeworkID=$homeworkID")
    }

    override suspend fun cancelHomeworkSubmission(
        homeworkID: String,
        studentID: String
    ) {
        val response = homeworkAPIService.cancelHomeworkSubmission(
            CancelHomeworkRequest(
                userId = studentID,
                homeworkId = homeworkID
            )
        )

        LollipopAPIHelper.handleAPIResponse(response)

        Log.i("LollipopRepo", "宿題提出のキャンセルに成功: homeworkID=$homeworkID")
    }
}
