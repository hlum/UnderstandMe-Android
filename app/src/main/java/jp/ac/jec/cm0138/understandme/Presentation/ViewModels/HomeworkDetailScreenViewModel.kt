package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.jec.cm0138.understandme.Entity.HomeworkState
import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus
import jp.ac.jec.cm0138.understandme.Entity.ResultData
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCase
import jp.ac.jec.cm0138.understandme.UseCase.HomeworkUseCase
import jp.ac.jec.cm0138.understandme.UseCase.ResultUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeworkDetailScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val homeworkUseCase: HomeworkUseCase,
    private val classUseCase: ClassUseCase,
    private val resultUseCase: ResultUseCase
): ViewModel() {

    val TAG = "HomeworkDetailViewModel"

    var homework by mutableStateOf<HomeworkWithStatus?>(null)
    var className by mutableStateOf<String?>(null)
    var result by mutableStateOf<ResultData?>(null)

    var projectLink by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var showErrorMessage by mutableStateOf(false)


    fun loadData(homeworkID: String) {
        viewModelScope.launch {
            loadHomework(homeworkID)

            val classID = homework?.classID

            // Load class name
            if (classID != null) {
                loadClassName(classID)
            }

            // 回答完了している場合は結果も読み込む
            if(homework?.submissionState == HomeworkState.completed) {
                loadResult()
            }
        }
    }


    fun regenerateQuestions(homeworkID: String) {
        viewModelScope.launch {
            val authUser = authRepository.getCurrentUser()
            homeworkUseCase.retryQuestionGeneration(
                homeworkID = homeworkID,
                studentID = authUser.uid
            )

            reload()
        }
    }


    fun cancelSubmission(homeworkID: String) {
        viewModelScope.launch {
            val authUser = authRepository.getCurrentUser()
            homeworkUseCase.cancelHomeworkSubmission(
                homeworkID = homeworkID,
                studentID = authUser.uid
            )

            reload()
        }
    }   


    fun reload() {
        viewModelScope.launch {
            val homeworkID = homework?.id ?: return@launch
            loadHomework(homeworkID)
        }
    }

    private suspend fun loadHomework(homeworkID: String) {

        val authUser = authRepository.getCurrentUser()

           try {
               isLoading = true
               homework = homeworkUseCase.fetchHomework(authUser.uid, homeworkID)
           } catch (e: Exception) {
               Log.e(TAG, "Error loading homework details", e)
           } finally {
               isLoading = false
           }
    }


    private suspend fun loadResult() {
        val homework = homework ?: return
        val authUser = authRepository.getCurrentUser()

            try {
                isLoading = true
                result = resultUseCase.fetchResult(
                    authUser.uid,
                    homework.id
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error loading result", e)
            } finally {
                isLoading = false
            }
    }

    fun uploadProject() {
        showErrorMessage = false

        if(!projectLinkIsValid(projectLink)) {
            return
        }

        val homework = homework

        if (homework == null) {
            showInputError("宿題の情報が見つかりません。")
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                val authUser = authRepository.getCurrentUser()
                homeworkUseCase.uploadProject(authUser.uid, homework.id, projectLink)
                reload()
            }catch (e: Exception) {
                Log.e(TAG, "Error uploading project", e)
                showInputError("プロジェクトの提出に失敗しました。")
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun loadClassName(classID: String) {
            try {
                className = classUseCase.fetchClass(classID).name
            } catch (e: Exception) {
                Log.e(TAG, "Error loading class name", e)
            }
    }


    private fun projectLinkIsValid(link: String): Boolean {
        if (link.isBlank()) {
            showInputError("プロジェクトリンクを入力してください。")
            return false
        }

        if (!android.util.Patterns.WEB_URL.matcher(link).matches()) {
            showInputError("有効なURLを入力してください。")
            return false
        }

        // GitHub / Google Drive check
        if (!isGithubOrGoogleDriveLink(link)) {
            showInputError("GitHub または Google Drive のリンクを入力してください。")
            return false
        }

        return true
    }


    private fun isGithubOrGoogleDriveLink(link: String): Boolean {
        return try {
            val uri = android.net.Uri.parse(link)
            val host = uri.host ?: return false

            host.contains("github.com", ignoreCase = true) ||
                    host.contains("drive.google.com", ignoreCase = true)
        } catch (e: Exception) {
            false
        }
    }



    private fun showInputError(message: String) {
        showErrorMessage = true
        errorMessage = message
    }
}