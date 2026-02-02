package jp.ac.jec.cm0138.understandme.Repository.Impl

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.Class
import jp.ac.jec.cm0138.understandme.Helper.LollipopAPIHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ClassRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.AddOptionalClassRequest
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ClassAPIService
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCaseError


sealed class ClassRepositoryError(message: String) : Exception(message) {
    class NotFound : ClassRepositoryError("Class not found")
}


class LollipopClassRepository @Inject constructor(
    private val classAPIService: ClassAPIService
): ClassRepository {


    override suspend fun attendOptionalClass(userID: String, classCode: String) {
        val request = AddOptionalClassRequest(userID, classCode)
        val response = classAPIService.attendToOptionalClass(request)

        LollipopAPIHelper.handleAPIResponse(response)
    }


    override suspend fun fetchClassWithClassCode(classCode: String): Class {
        val response = classAPIService.fetchClassWithClassCode(classCode)

        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)
        if(apiResponse.data.isNullOrEmpty()) {
            throw ClassRepositoryError.NotFound()
        }

        return apiResponse.data.first()
    }


    override suspend fun fetchClassWithID(id: String): Class {
        val response = classAPIService.fetchClassWithID(id)

        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)
        return apiResponse.data?.firstOrNull() ?: throw Exception("Class not found")
    }


    override suspend fun fetchAllClasses(userID: String): List<Class> {
        val response = classAPIService.fetchAllClass(userID)

        val apiResponse = LollipopAPIHelper.handleAPIResponse(response)

        return apiResponse.data ?: throw IllegalStateException("No classes found")
    }
}