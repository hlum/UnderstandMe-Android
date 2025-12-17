package jp.ac.jec.cm0138.understandme.UseCase

import android.util.Log
import jp.ac.jec.cm0138.understandme.Entity.Class
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ClassRepository
import javax.inject.Inject

sealed class ClassUseCaseError(message: String) : Exception(message) {

    class InvalidClassCode : ClassUseCaseError("無効な学科コードです。")

    class AlreadyEnrolled : ClassUseCaseError("すでにこの科目に登録されています。")

    class UnknownError : ClassUseCaseError("予期せぬエラーが発生しました。やり直してください。")
}


class ClassUseCase @Inject constructor(
    private val classRepository: ClassRepository
) {

    private val TAG = "ClassUseCase"


    suspend fun attendOptionalClass(
        classCode: String,
        userID: String
    ) {
        checkClassCode(classCode, userID)
        classRepository.attendOptionalClass(userID, classCode)
    }


    suspend fun fetchClass(id: String): Class {
        return classRepository.fetchClassWithID(id)
    }


    suspend fun fetchClassWithClassCode(classCode: String): Class? {
        return classRepository.fetchClassWithClassCode(classCode)
    }


    suspend fun fetchClassList(studentID: String): List<Class> {
        return classRepository.fetchAllClasses(studentID)
    }


    private suspend fun checkClassCode(
        classCode: String,
        userID: String
    ) {
        val classExists = try {
            fetchClassWithClassCode(classCode) != null
        } catch (e: Exception) {
            Log.e(TAG, "学科コード検証中に学科取得失敗", e)
            throw ClassUseCaseError.UnknownError()
        }

        if (!classExists) {
            throw ClassUseCaseError.InvalidClassCode()
        }

        val alreadyEnrolled = try {
            fetchClassList(userID).any { it.classCode == classCode }
        } catch (e: Exception) {
            Log.e(TAG, "履修済みチェック中に学科取得失敗", e)
            throw ClassUseCaseError.UnknownError()
        }

        if (alreadyEnrolled) {
            throw ClassUseCaseError.AlreadyEnrolled()
        }
    }
}
