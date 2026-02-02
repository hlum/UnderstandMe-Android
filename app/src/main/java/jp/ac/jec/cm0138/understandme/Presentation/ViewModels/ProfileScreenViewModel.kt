package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.jec.cm0138.understandme.Entity.ResultData
import jp.ac.jec.cm0138.understandme.Entity.UserData
import jp.ac.jec.cm0138.understandme.Presentation.Screens.Components.MonthlyAverageResult
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.UseCase.ResultUseCase
import jp.ac.jec.cm0138.understandme.UseCase.UserDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.Year
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val resultUseCase: ResultUseCase,
    private val userDataUseCase: UserDataUseCase
) : ViewModel() {
    val TAG = "ProfileScreenVM"


    var userData by mutableStateOf<UserData?>(null)
        private set

    var allResults by mutableStateOf<List<ResultData>>(emptyList())
        private set

    var averageScoreOfAllTime by mutableStateOf(0)

    var monthlyAverageResult by mutableStateOf<List<MonthlyAverageResult>>(emptyList())

    var isLoading by mutableStateOf(false)
        private set

    var currentSelectedYearForGraph by mutableStateOf<Int>(Year.now().value)


    fun loadProfileData() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true

            try {
                loadProfileDataInternally()
                calculateAverageScores()
                calculateAverageScoreOfAllTime()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading profile datas: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


    fun onNextYearButtonClicked() {
        if (currentSelectedYearForGraph < Year.now().value) {
            currentSelectedYearForGraph += 1
            calculateAverageScores()
        }
    }


    fun onPreviousYearButtonClicked() {
        currentSelectedYearForGraph -= 1
        calculateAverageScores()
    }


    private suspend fun deleteFCMToken(context: Context) {
        try {
            val userID = authRepository.getCurrentUser().uid
            withContext(Dispatchers.IO) {
                userDataUseCase.deleteFCMToken(context = context, userID = userID)
            }
            Log.d(TAG, "FCM token deleted successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete FCM token", e)
        }
    }

    private suspend fun loadProfileDataInternally() = coroutineScope {

        val currentUser = authRepository.getCurrentUser()

        val userDeferred = async(Dispatchers.IO) {
            userDataUseCase.fetchUserData(currentUser.uid)
        }

        val resultsDeferred = async(Dispatchers.IO) {
            resultUseCase.fetchResults(
                userID = currentUser.uid,
            )
        }

        // Await results on Main thread
        userData = userDeferred.await()

        allResults = resultsDeferred.await()

    }


    private fun calculateAverageScoreOfAllTime() {
        averageScoreOfAllTime = if (allResults.isNotEmpty()) {
            allResults
                .map { it.score }
                .average()
                .toInt()
        } else {
            0
        }
    }

    private fun calculateAverageScores() {

        monthlyAverageResult = (1..12).map { month ->
            val resultsForMonth = allResults.filter {
                it.evaluatedAtDate.month.value == month &&
                        it.evaluatedAtDate.year == currentSelectedYearForGraph
            }

            MonthlyAverageResult(
                month = LocalDateTime.of(
                    currentSelectedYearForGraph,
                    month,
                    1,
                    0,
                    0
                ),
                averageScore = resultsForMonth
                    .map { it.score }
                    .average()
                    .toInt()
            )
        }
    }


    fun signOut(context: Context) {
        viewModelScope.launch {
            try {
                isLoading = true
                deleteFCMToken(context)
                authRepository.logOut()
            } catch (e: Exception) {
                Log.e(TAG, "Error signing out: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteAccount(context: Context) {
        viewModelScope.launch {
            try {
                isLoading = true
                val currentUser = authRepository.getCurrentUser()

                withContext(Dispatchers.IO) {
                    userDataUseCase.deleteUserData(currentUser.uid)
                    authRepository.logOut()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting account: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

}