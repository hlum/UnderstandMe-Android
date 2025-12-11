package jp.ac.jec.cm0138.understandme.Screens

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthenticationiRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.AuthResult
import jp.ac.jec.cm0138.understandme.ViewModels.AuthState
import jp.ac.jec.cm0138.understandme.ViewModels.LoginScreenViewModel
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import jp.ac.jec.cm0138.understandme.customTheme.customPrimaryButtonColors


@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val loginUIState by viewModel.loginUiState.collectAsState()

    val activity = LocalContext.current

    LaunchedEffect(loginUIState.authState) {
        when (loginUIState.authState) {
            AuthState.Failed -> {
                onShowSnackbar("サインインに失敗しました。もう一度お試しください")
            }

            AuthState.Success -> {
                onShowSnackbar("サインインに成功しました。")
            }

            AuthState.Cancelled -> {
                onShowSnackbar("サインインがキャンセルされました")
            }

            else -> Unit
        }
    }

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
                viewModel.signInWithGoogle(activity)
            },
            colors = ButtonDefaults.customPrimaryButtonColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(10.dp),
            enabled = loginUIState.authState != AuthState.Loading
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google Sign in",
                    modifier = Modifier
                        .width(20.dp)
                )


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


class PreviewAuthRepository() : AuthenticationiRepository {
    override suspend fun signInWithGoogle(context: Context): AuthResult {
        return AuthResult.Success
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()

    Scaffold { innerpadding ->
        LoginScreen(
            modifier = Modifier.padding(innerpadding),
            viewModel = LoginScreenViewModel(authenticationRepository = PreviewAuthRepository()),
            onShowSnackbar = { message -> },
            navController = navController
        )


    }
}
