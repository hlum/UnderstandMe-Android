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
            
            if (authResult == AuthResult.Success) {
                    registerUser()
                    updateAuthState(AuthState.fromAuthResult(authResult))
            }

        }
    }


    // Register new user only
    suspend fun registerUser() {
            val user = authenticationRepository.getCurrentUser()

            val email = user.email
            if(email == null) {
                Log.e("LoginScreenViewModel", "registerUser: user.email が nullになってます。")
                return
            }


            val (studentCode, className, admissionYear) =
                extractStudentInfo(email)

            // Get photoURL from Google provider
            val googleProvider = user.providerData.find { it.providerId == "google.com" }
            val photoURL = googleProvider?.photoUrl?.toString()

            val userData = UserData(
                id = user.uid,
                name = user.displayName ?: email,
                email = email,
                studentCode = studentCode,
                majorCode = className,
                admissionYear = admissionYear,
                photoURL = photoURL
            )

        try {
            userDataUseCase.registerUserIfNotExists(userData)
        } catch(e: Exception) {
            Log.e("LoginScreenViewModel", "registerUser: ユーザーデータの登録に失敗しました。再試行します。", e)
        }

    }


    // メールから学年と学科コードを取得する
    // 学校メールでない場合はダミーを返す
    fun extractStudentInfo(email: String): Triple<String, String, Int> {
        // Example valid format: "24cm0138@jec.ac.jp"

        val atIndex = email.indexOf("@")
        if (atIndex == -1) {
            return Triple("99zz", "zz", 99)
        }

        // Get part before "@"
        val localPart = email.substring(0, atIndex)

        if (localPart.length < 4) {
            return Triple("99zz", "zz", 99)
        }

        val yearPart = localPart.take(2)
        val admissionYear = yearPart.toIntOrNull()
            ?: return Triple("99zz", "zz", 99)

        val classPart = localPart.drop(2)
        val className = classPart.takeWhile { it.isLetter() }

        if (className.length != 2) {
            return Triple("99zz", "zz", 99)
        }

        val studentCode = localPart

        return Triple(studentCode, className, admissionYear)
    }



    private fun updateAuthState(value: AuthState) {
        _loginUiState.update {
            it.copy(authState = value)
        }
    }
}