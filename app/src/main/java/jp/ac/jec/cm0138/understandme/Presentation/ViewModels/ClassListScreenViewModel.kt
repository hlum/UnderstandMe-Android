package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

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
                // Handle error appropriately
            } finally {
                isLoading = false
            }
        }
    }
}