package jp.ac.jec.cm0138.understandme.Presentation.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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


    BackHandler(enabled = true) {
        // do nothing
    }


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

            AuthState.NoCredentialAvailable -> {
                onShowSnackbar("Googleアカウントでログインしているか、デバイスのロック画面（PIN、パターン、パスワード）が設定されているかを確認してください")
            }

            else -> Unit
        }
    }

    // Background gradient matching iOS - ignore parent padding for full screen coverage
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        MyAppTheme.colors.accent.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // App icon and title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.applogo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(120.dp)
                )

                Text(
                    text = "Know Your Code",
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Google Sign In Button - Following Official Google Guidelines
            Button(
                onClick = {
                    viewModel.signInWithGoogle(activity)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F5F5), // Light gray background
                    contentColor = Color(0xFF3C4043), // Dark gray text
                    disabledContainerColor = Color(0xFFE0E0E0),
                    disabledContentColor = Color(0xFF9E9E9E)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFDADCE0),
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                enabled = loginUIState.authState != AuthState.Loading,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                if (loginUIState.authState == AuthState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFF3C4043)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = "Google Logo",
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Googleでサインイン",
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
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
