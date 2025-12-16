package jp.ac.jec.cm0138.understandme.Repository.Impl

import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.Class
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ClassRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.AddOptionalClassRequest
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ClassAPIService

class LollipopClassRepository @Inject constructor(
    private val classAPIService: ClassAPIService
): ClassRepository {


    override suspend fun attendOptionalClass(userID: String, classCode: String) {
        val request = AddOptionalClassRequest(userID, classCode)
        val response = classAPIService.attendToOptionalClass(request)

        if(response.status != "success") {
            throw Exception("Error: ${response.message}")
        }
    }


    override suspend fun fetchClassWithClassCode(classCode: String): Class {
        val response = classAPIService.fetchClassWithClassCode(classCode)

        if(response.status != "success") {
            throw Exception("Error: ${response.message}")
        }

        val list = response.data ?: throw Exception("No data")

        return list.firstOrNull() ?: throw Exception("Class not found")
    }


    override suspend fun fetchClassWithID(id: String): Class {
        val response = classAPIService.fetchClassWithID(id)

        if(response.status != "success") {
            throw Exception("Error: ${response.message}")
        }

        val list = response.data ?: throw Exception("No data")

        return list.firstOrNull() ?: throw Exception("Class not found")
    }


    override suspend fun fetchAllClasses(userID: String): List<Class> {
        val response = classAPIService.fetchAllClass(userID)

        if(response.status != "success") {
            throw Exception("Error: ${response.message}")
        }

        val list = response.data ?: throw Exception("No data")

        return list
    }
}