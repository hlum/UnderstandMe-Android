package jp.ac.jec.cm0138.understandme.Repository.Abstract
import jp.ac.jec.cm0138.understandme.Entity.Class

interface ClassRepository {
    suspend fun attendOptionalClass(userID: String, classCode: String)
    suspend fun fetchClassWithClassCode(classCode: String): Class
    suspend fun fetchClassWithID(id: String): Class
    suspend fun fetchAllClasses(userID: String): List<Class>
}