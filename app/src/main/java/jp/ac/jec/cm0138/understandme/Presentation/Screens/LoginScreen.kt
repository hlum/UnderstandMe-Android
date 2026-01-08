package jp.ac.jec.cm0138.understandme.Presentation.Screens

import android.annotation.SuppressLint
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
import jp.ac.jec.cm0138.understandme.Entity.UserData
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.HOME_ROUTE
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.AuthState
import jp.ac.jec.cm0138.understandme.Presentation.ViewModels.LoginScreenViewModel
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.Repository.Abstract.FCMTokenRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.UserDataRepository
import jp.ac.jec.cm0138.understandme.Repository.TestRepo.TestAuthRepository
import jp.ac.jec.cm0138.understandme.UseCase.UserDataUseCase
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
                navController.navigate(HOME_ROUTE)
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
                fontSize = 50.sp, fontWeight = FontWeight.Bold
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
                    modifier = Modifier.width(20.dp)
                )


                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Googleでサインイン",
                    color = MyAppTheme.colors.background,
                    style = TextStyle(
                        fontSize = 20.sp, fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}


class PreviewUserDataRepository() : UserDataRepository {
    override suspend fun saveUserData(userData: UserData) {
        return
    }

    override suspend fun fetchUserData(userID: String): UserData {
        return UserData.getDummy()
    }

    override suspend fun updateFCMToken(userID: String, fcmToken: String) {
        return
    }
}

class PreviewFCMTokenRepository() : FCMTokenRepository {
    override suspend fun saveOrUpdateToken(userID: String, deviceID: String, deviceType: String, fcmToken: String) {
        return
    }

    override suspend fun deleteFcmToken(userID: String, deviceID: String) {
        return
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()

    Scaffold { innerpadding ->
        LoginScreen(
            modifier = Modifier.padding(innerpadding), viewModel = LoginScreenViewModel(
                authenticationRepository = TestAuthRepository(),
                userDataUseCase = UserDataUseCase(
                    userDataRepository = PreviewUserDataRepository(),
                    fcmTokenRepository = PreviewFCMTokenRepository()
                )
            ), onShowSnackbar = { message -> }, navController = navController
        )


    }
}
