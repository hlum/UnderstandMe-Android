package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.jec.cm0138.understandme.Entity.Class
import jp.ac.jec.cm0138.understandme.Entity.HomeworkState
import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCase
import jp.ac.jec.cm0138.understandme.UseCase.HomeworkUseCase
import jp.ac.jec.cm0138.understandme.UseCase.UserDataUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val userDataUseCase: UserDataUseCase,
    private val authRepository: AuthRepository,
    private val classUseCase: ClassUseCase,
    private val homeworkUseCase: HomeworkUseCase
) : ViewModel() {
    val TAG = "HomeScreenViewModel"

    var classList by mutableStateOf<List<Class>>(emptyList())
        private set

    var homeworks by mutableStateOf<List<HomeworkWithStatus>>(emptyList())
        private set


    fun loadClassesAndHomeworks() {
        viewModelScope.launch {
            loadClassList()
            loadHomeworks()
        }
    }

    private fun loadClassList() {
        val currentUser = authRepository.getCurrentUser()
        viewModelScope.launch {
            try {
                val classes = classUseCase.fetchClassList(currentUser.uid)
                classList = classes
            }catch (e: Exception) {
                Log.e(TAG, "loadClassList: $e")
            }
        }
    }


    private fun loadHomeworks() {
        viewModelScope.launch {

            val authUser = authRepository.getCurrentUser()

            try {
                var result = homeworkUseCase.fetchAllHomeworks(
                    studentID = authUser.uid
                )

                // 1️⃣ Filter (not COMPLETED)
                result = result.filter {
                    it.submissionState != HomeworkState.completed
                }

                // 2️⃣ Sort: dueDate asc → submissionState priority
                val submissionStatePriority = mapOf(
                    HomeworkState.notAssigned to 0,
                    HomeworkState.failed to 1,
                    HomeworkState.questionGenerated to 2,
                    HomeworkState.generatingQuestions to 3
                )

                result = result.sortedWith { lhs, rhs ->
                    val lhsDate = lhs.dueDate ?: LocalDate.MAX
                    val rhsDate = rhs.dueDate ?: LocalDate.MAX

                    if (lhsDate != rhsDate) {
                        lhsDate.compareTo(rhsDate)
                    } else {
                        val lhsPriority =
                            submissionStatePriority[lhs.submissionState] ?: Int.MAX_VALUE
                        val rhsPriority =
                            submissionStatePriority[rhs.submissionState] ?: Int.MAX_VALUE

                        lhsPriority.compareTo(rhsPriority)
                    }
                }

                homeworks = result

            } catch (e: Exception) {
                Log.e(
                    "HomeViewModel",
                    "HomeViewModel.loadHomeworks(): 宿題の取得に失敗しました。",
                    e
                )
            }
        }
    }
}