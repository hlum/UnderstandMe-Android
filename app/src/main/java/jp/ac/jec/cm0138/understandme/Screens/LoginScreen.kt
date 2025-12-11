package jp.ac.jec.cm0138.understandme.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthenticationiRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.AuthResult
import jp.ac.jec.cm0138.understandme.Repository.Impl.FirebaseAuthenticationRepository
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import jp.ac.jec.cm0138.understandme.customTheme.customPrimaryButtonColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// AuthRepository -> AuthResult -> ViewModel用AuthState
enum class AuthState {
    Idle,
    Loading,
    Success,
    Cancelled,
    Failed
}

class LoginScreenViewModel(val authenticationRepository: AuthenticationiRepository) : ViewModel() {
    private val _authState = MutableStateFlow(AuthState.Idle)
    val authState = _authState.asStateFlow()


    fun signInWithGoogle() {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {

                val authResult = authenticationRepository.signInWithGoogle()

                when (authResult) {
                    is AuthResult.Success -> {
                        _authState.value = AuthState.Success
                    }

                    is AuthResult.Cancelled -> {
                        _authState.value = AuthState.Cancelled
                    }
                }

            } catch (e: Exception) {
                _authState.value = AuthState.Failed
            }
        }

    }
}

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = LoginScreenViewModel(
        authenticationRepository = FirebaseAuthenticationRepository(context = LocalContext.current)
    ),
    modifier: Modifier = Modifier
) {
    val loginState by viewModel.authState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    LaunchedEffect(loginState) {
        Log.i("TEST", "here")
        when (loginState) {
            AuthState.Failed -> {

                snackbarHostState.showSnackbar("サインインに失敗しました。もう一度お試しください")
            }

            AuthState.Success -> {
                snackbarHostState.showSnackbar("サインいんに成功しました。")
            }

            AuthState.Cancelled -> {
                snackbarHostState.showSnackbar("サインインがキャンセルされました")
            }

            else -> Unit
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Understand Me",
                modifier = Modifier,
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Google SignIn Button
            Button(
                onClick = {
                    viewModel.signInWithGoogle()
                },
                colors = ButtonDefaults.customPrimaryButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = loginState != AuthState.Loading
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    if(loginState != AuthState.Loading) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = "Google Sign in",
                            modifier = Modifier
                                .width(20.dp)
                        )
                    } else {
                        CircularProgressIndicator(modifier = Modifier.width(8.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Googleでサインイン",
                        color = MyAppTheme.colors.background,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Scaffold { innerpadding ->
        LoginScreen(modifier = Modifier.padding(innerpadding))
    }
}