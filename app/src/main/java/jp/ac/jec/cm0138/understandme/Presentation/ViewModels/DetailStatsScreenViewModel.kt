package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Entity.AverageScorePerClass
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.UseCase.AverageScoreUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class DetailStatsScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val averageScoreUseCase: AverageScoreUseCase
): ViewModel() {

    val TAG = "DetailStatsScreenVM"

    var isLoading by mutableStateOf(false)
        private set

    var averageScoresPerClass by mutableStateOf<List<AverageScorePerClass>>(emptyList())
        private set


    fun loadAverageScoresPerClass() {
        isLoading = true

        viewModelScope.launch {
            try {
                val averageScores = withContext(Dispatchers.IO) {
                    val currentUser = authRepository.getCurrentUser()
                    averageScoreUseCase.fetchAverageScores(
                        userID = currentUser.uid
                    )
                }
                averageScoresPerClass = averageScores
            } catch (e: Exception) {
                Log.e(TAG, "Error loading average scores per class: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

}