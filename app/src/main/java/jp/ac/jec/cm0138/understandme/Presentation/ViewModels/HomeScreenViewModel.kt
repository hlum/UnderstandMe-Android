package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.jec.cm0138.understandme.Entity.Class
import jp.ac.jec.cm0138.understandme.Entity.HomeworkState
import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus
import jp.ac.jec.cm0138.understandme.Entity.UserData
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCase
import jp.ac.jec.cm0138.understandme.UseCase.HomeworkUseCase
import jp.ac.jec.cm0138.understandme.UseCase.UserDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    var currentUser by mutableStateOf<UserData?>(null)



fun logout() {
    viewModelScope.launch {
        authRepository.logOut()
    }
}

    fun loadData() {
        if (isLoading) return
        viewModelScope.launch {
            isLoading = true

            try {
                // Run heavy work OFF the main thread
                val (user, classes, homeworksResult) = withContext(Dispatchers.IO) {
                    coroutineScope {
                        val userDeferred = async { fetchCurrentUser() }
                        val classesDeferred = async { fetchClassList() }
                        val homeworksDeferred = async { fetchAndProcessHomeworks() }

                        Triple(
                            userDeferred.await(),
                            classesDeferred.await(),
                            homeworksDeferred.await()
                        )
                    }
                }

                // Apply state on MAIN thread
                currentUser = user
                classList = classes
                homeworks = homeworksResult

            } catch (e: Exception) {
                Log.e(TAG, "loadClassesAndHomeworks", e)
            } finally {
                isLoading = false
            }
        }
    }


    fun saveFCMToken() {
        // TODO: Implement saving FCM token when the homeScreen is loaded
    }

    private suspend fun fetchCurrentUser(): UserData {
        val currentAuthData = authRepository.getCurrentUser()
        return userDataUseCase.fetchUserData(currentAuthData.uid)
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