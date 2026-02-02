package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.util.Log
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.Class
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ClassRepository
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCase
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCaseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

@HiltViewModel
class ClassListScreenViewModel @Inject constructor(
    private val authenticationRepository: AuthRepository,
    private val classUseCase: ClassUseCase
): ViewModel() {
    var classes by mutableStateOf<List<Class>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var classCode by mutableStateOf("")
        private set

    var classCodeErrorMessage by mutableStateOf("")
        private set

    var showAddOptionalClassSheet by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    var showErrorAlert by mutableStateOf(false)
        private set

    fun updateClassCode(code: String) {
        classCode = code
        classCodeErrorMessage = ""
    }

    fun toggleAddOptionalClassSheet(show: Boolean) {
        showAddOptionalClassSheet = show
        if (!show) {
            classCode = ""
            classCodeErrorMessage = ""
        }
    }

    fun dismissErrorAlert() {
        showErrorAlert = false
        errorMessage = ""
    }

    fun addOptionalClass() {
        if (classCode.isEmpty()) {
            classCodeErrorMessage = "学科コードを入力してください！"
            return
        }

        viewModelScope.launch {
            try {
                val currentUser = withContext(Dispatchers.IO) {
                    authenticationRepository.getCurrentUser()
                }

                withContext(Dispatchers.IO) {
                    classUseCase.attendOptionalClass(classCode, currentUser.uid)
                }

                toggleAddOptionalClassSheet(false)
                loadClasses()

            } catch (e: ClassUseCaseError.InvalidClassCode) {
                classCodeErrorMessage = "学科コードが無効です。"
            } catch (e: ClassUseCaseError.AlreadyEnrolled) {
                classCodeErrorMessage = "すでにこの科目に登録されています。"
            } catch (e: ClassUseCaseError.UnknownError) {
                classCodeErrorMessage = e.message ?: "予期せぬエラーが発生しました。"
            } catch (e: Exception) {
                classCodeErrorMessage = "予期せぬエラーが発生しました、もう一度やり直してください。"
                Log.e("ClassListScreenViewModel", "addOptionalClass error", e)
            }
        }
    }

    fun loadClasses() {
        if(isLoading) return

        viewModelScope.launch {
            isLoading = true

            try {
                val classList = withContext(Dispatchers.IO) {
                    val currentUser = authenticationRepository.getCurrentUser()
                    classUseCase.fetchClassList(currentUser.uid)
                }

                classes = classList

            } catch (e: Exception) {
                errorMessage = "クラス一覧の取得に失敗しました。"
                showErrorAlert = true
                Log.e("ClassListScreenViewModel", "loadClasses error", e)
            } finally {
                isLoading = false
            }
        }
    }
}