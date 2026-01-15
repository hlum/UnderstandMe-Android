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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    loadHomeworkInternal(homeworkID)
                }

                val classID = homework?.classID

                // Load class name
                if (classID != null) {
                    withContext(Dispatchers.IO) {
                        loadClassNameInternal(classID)
                    }
                }

                // 回答完了している場合は結果も読み込む
                if(homework?.submissionState == HomeworkState.completed) {
                    withContext(Dispatchers.IO) {
                        loadResultInternal()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading data", e)
            } finally {
                isLoading = false
            }
        }
    }


    fun regenerateQuestions(homeworkID: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    val authUser = authRepository.getCurrentUser()
                    homeworkUseCase.retryQuestionGeneration(
                        homeworkID = homeworkID,
                        studentID = authUser.uid
                    )
                }
                reload()
            } catch (e: Exception) {
                Log.e(TAG, "Error regenerating questions", e)
            } finally {
                isLoading = false
            }
        }
    }


    fun cancelSubmission(homeworkID: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    val authUser = authRepository.getCurrentUser()
                    homeworkUseCase.cancelHomeworkSubmission(
                        homeworkID = homeworkID,
                        studentID = authUser.uid
                    )
                }
                reload()
            } catch (e: Exception) {
                Log.e(TAG, "Error cancelling submission", e)
            } finally {
                isLoading = false
            }
        }
    }   


    fun reload() {
        viewModelScope.launch {
            val homeworkID = homework?.id ?: return@launch
            isLoading = true
            try {
                withContext(Dispatchers.IO) {
                    loadHomeworkInternal(homeworkID)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error reloading homework", e)
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun loadHomeworkInternal(homeworkID: String) {
        val authUser = authRepository.getCurrentUser()
        val loadedHomework = homeworkUseCase.fetchHomework(authUser.uid, homeworkID)
        withContext(Dispatchers.Main) {
            homework = loadedHomework
        }
    }


    private suspend fun loadResultInternal() {
        val hw = homework ?: return
        val authUser = authRepository.getCurrentUser()
        val loadedResult = resultUseCase.fetchResult(authUser.uid, hw.id)
        withContext(Dispatchers.Main) {
            result = loadedResult
        }
    }

    fun uploadProject() {
        showErrorMessage = false

        if(!projectLinkIsValid(projectLink)) {
            return
        }

        val hw = homework

        if (hw == null) {
            showInputError("宿題の情報が見つかりません。")
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                withContext(Dispatchers.IO) {
                    val authUser = authRepository.getCurrentUser()
                    homeworkUseCase.uploadProject(authUser.uid, hw.id, projectLink)
                }
                reload()
            }catch (e: Exception) {
                Log.e(TAG, "Error uploading project", e)
                showInputError("プロジェクトの提出に失敗しました。")
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun loadClassNameInternal(classID: String) {
        val name = classUseCase.fetchClass(classID).name
        withContext(Dispatchers.Main) {
            className = name
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
            showInputError("有効なGitHubリポジトリURL (https://github.com/ユーザー名/リポジトリ名) または Google Drive のリンクを入力してください。")
            return false
        }

        return true
    }


    /// Validates GitHub repository URL
    /// Valid formats:
    /// - https://github.com/username/repository
    /// - https://github.com/username/repository/
    private fun isGithubOrGoogleDriveLink(link: String): Boolean {
        return try {
            val uri = android.net.Uri.parse(link)
            val host = uri.host ?: return false

            when {
                host.equals("github.com", ignoreCase = true) -> {
                    // Validate that the URL is a valid repository URL for git clone
                    // Must have exactly 2 path segments: username and repository
                    val pathSegments = uri.pathSegments
                    pathSegments.size == 2
                }
                host.contains("drive.google.com", ignoreCase = true) -> {
                    // Google Drive links are accepted as-is
                    true
                }
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }



    private fun showInputError(message: String) {
        showErrorMessage = true
        errorMessage = message
    }
}