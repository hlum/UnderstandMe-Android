package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.jec.cm0138.understandme.Entity.HomeworkFilterOption
import jp.ac.jec.cm0138.understandme.Entity.HomeworkState
import jp.ac.jec.cm0138.understandme.Entity.HomeworkWithStatus
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.HomeworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class HomeworkListScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val homeworkRepository: HomeworkRepository
): ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var allHomeworks by mutableStateOf<List<HomeworkWithStatus>>(emptyList())
        private set

    var filteredHomeworks by mutableStateOf<List<HomeworkWithStatus>>(emptyList())
        private set

    var selectedFilterOption by mutableStateOf<HomeworkFilterOption>(HomeworkFilterOption.All)
        private set

    var searchText by mutableStateOf("")
        private set


    fun loadHomeworks() {
        viewModelScope.launch {
            isLoading = true

            try {
                val homeworks = withContext(Dispatchers.IO) {
                    val currentUser = authRepository.getCurrentUser()
                    homeworkRepository.fetchHomeworks(currentUser.uid)
                }
                allHomeworks = homeworks
                applyFiltersAsync()
            } catch (e: Exception) {

            } finally {
                isLoading = false
            }
        }
    }

    fun handleFilterChange(option: HomeworkFilterOption) {
        selectedFilterOption = option
        applyFiltersAsync()
    }

    fun handleSearchTextChange(text: String) {
        searchText = text
        applyFiltersAsync()
    }


    fun applyFiltersAsync() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) {
                applyFiltersInternal()
            }
            filteredHomeworks = result
        }
    }

    private fun applyFiltersInternal(): List<HomeworkWithStatus> {
        var result = allHomeworks

        // ① State filter
        result = when (val option = selectedFilterOption) {
            is HomeworkFilterOption.All -> result
            is HomeworkFilterOption.State ->
                result.filter { it.submissionState == option.homeworkState }
        }

        // ② Search filter
        if (searchText.isNotBlank()) {
            result = result.filter {
                it.title.contains(searchText, ignoreCase = true)
            }
        }

        // ③ Sort
        return sortHomeworks(result, selectedFilterOption)
    }



    private fun sortHomeworks(
        homeworks: List<HomeworkWithStatus>,
        option: HomeworkFilterOption
    ): List<HomeworkWithStatus> {
        return when (option) {
            is HomeworkFilterOption.All -> {
                homeworks.sortedByDescending { it.createdAt }
            }

            is HomeworkFilterOption.State -> {
                if (option.homeworkState == HomeworkState.notAssigned) {
                    homeworks.sortedWith(
                        compareBy<HomeworkWithStatus> { it.dueDate == null } // nulls last
                            .thenBy { it.dueDate }                           // earlier first
                    )
                } else {
                    homeworks
                }
            }
        }
    }


}