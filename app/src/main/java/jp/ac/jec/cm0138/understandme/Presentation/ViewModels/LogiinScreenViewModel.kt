package jp.ac.jec.cm0138.understandme.Presentation.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.jec.cm0138.understandme.Entity.UserData
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.AuthResult
import jp.ac.jec.cm0138.understandme.UseCase.UserDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// AuthRepository -> AuthResult -> ViewModel用AuthState
enum class AuthState {
    Idle,
    Loading,
    Success,
    Cancelled,
    Failed,
    NoCredentialAvailable;

    companion object {
        fun fromAuthResult(authResult: AuthResult): AuthState {
            return when (authResult) {
                is AuthResult.Success -> Success
                is AuthResult.Cancelled -> Cancelled
                is AuthResult.Failed -> Failed
                is AuthResult.NoCredentialAvailable -> NoCredentialAvailable
            }
        }
    }
}

data class LoginUiState(
    val authState: AuthState = AuthState.Idle
)

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authenticationRepository: AuthRepository,
    private val userDataUseCase: UserDataUseCase
) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun signInWithGoogle(context: Context) {
        updateAuthState(AuthState.Loading)

        viewModelScope.launch {
            val authResult = authenticationRepository.signInWithGoogle(context)
            updateAuthState(AuthState.fromAuthResult(authResult))
        }
    }

    private fun updateAuthState(value: AuthState) {
        _loginUiState.update {
            it.copy(authState = value)
        }
    }
}