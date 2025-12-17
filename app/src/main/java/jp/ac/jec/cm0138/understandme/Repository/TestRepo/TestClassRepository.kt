package jp.ac.jec.cm0138.understandme.Repository.TestRepo

import jp.ac.jec.cm0138.understandme.Entity.Class
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ClassRepository

class TestClassRepository : ClassRepository {
    override suspend fun attendOptionalClass(userID: String, classCode: String) {
    }

    override suspend fun fetchClassWithClassCode(classCode: String): Class {
        return Class.getDummy()
    }

    override suspend fun fetchClassWithID(id: String): Class {
        return Class.getDummy()
    }

    override suspend fun fetchAllClasses(userID: String): List<Class> {
        return listOf(Class.getDummy(), Class.getDummy(), Class.getDummy(), Class.getDummy())
    }
}