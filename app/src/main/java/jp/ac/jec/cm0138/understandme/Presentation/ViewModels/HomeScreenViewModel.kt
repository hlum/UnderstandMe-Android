package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.jec.cm0138.understandme.Entity.Class
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.UseCase.ClassUseCase
import jp.ac.jec.cm0138.understandme.UseCase.UserDataUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val userDataUseCase: UserDataUseCase,
    private val authRepository: AuthRepository,
    private val classUseCase: ClassUseCase
) : ViewModel() {
    val TAG = "HomeScreenViewModel"

    var classList by mutableStateOf<List<Class>>(emptyList())
        private set

    fun loadClassList() {
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

}