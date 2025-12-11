package jp.ac.jec.cm0138.understandme.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthenticationiRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.AuthResult
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
    Failed
}

data class LoginUiState(
    val authState: AuthState = AuthState.Idle
)

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationiRepository
) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun signInWithGoogle(context: Context) {
        updateAuthState(AuthState.Loading)

        viewModelScope.launch {

            val authResult = authenticationRepository.signInWithGoogle(context)

            when (authResult) {
                is AuthResult.Success -> {
                    updateAuthState(AuthState.Success)
                }

                is AuthResult.Cancelled -> {
                    updateAuthState(AuthState.Cancelled)
                }

                is AuthResult.Failed -> {
                    updateAuthState(AuthState.Failed)
                }
            }
        }
    }


    private fun updateAuthState(value: AuthState) {
        _loginUiState.update {
            it.copy(authState = value)
        }
    }
}