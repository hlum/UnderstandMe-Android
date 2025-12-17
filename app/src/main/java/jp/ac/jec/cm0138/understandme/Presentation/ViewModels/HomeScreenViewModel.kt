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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
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

    var isLoading by mutableStateOf(false)
        private set


    fun loadClassesAndHomeworks() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true

            try {
                // Run heavy work OFF the main thread
                val (classes, homeworksResult) = withContext(Dispatchers.IO) {
                    val classesDeferred = async { fetchClassList() }
                    val homeworksDeferred = async { fetchAndProcessHomeworks() }

                    classesDeferred.await() to homeworksDeferred.await()
                }

                // Apply state on MAIN thread
                classList = classes
                homeworks = homeworksResult

            } catch (e: Exception) {
                Log.e(TAG, "loadClassesAndHomeworks", e)
            } finally {
                isLoading = false
            }
        }
    }



    private suspend fun fetchClassList(): List<Class> {
        val currentUser = authRepository.getCurrentUser()
        return classUseCase.fetchClassList(currentUser.uid)
    }



    private suspend fun fetchAndProcessHomeworks(): List<HomeworkWithStatus> =
        withContext(Dispatchers.Default) {

            val authUser = authRepository.getCurrentUser()
            var result = homeworkUseCase.fetchAllHomeworks(authUser.uid)

            result = result.filter {
                it.submissionState != HomeworkState.completed
            }

            val submissionStatePriority = mapOf(
                HomeworkState.notAssigned to 0,
                HomeworkState.failed to 1,
                HomeworkState.questionGenerated to 2,
                HomeworkState.generatingQuestions to 3
            )

            result.sortedWith { lhs, rhs ->
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
        }

}