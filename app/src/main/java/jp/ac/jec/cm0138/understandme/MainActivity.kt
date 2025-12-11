package jp.ac.jec.cm0138.understandme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import jp.ac.jec.cm0138.understandme.Navigation.AppNavigation
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthStateManager(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
): ViewModel() {

    private val _isLoginIn = MutableStateFlow((auth.currentUser != null))
    val isLogIn = _isLoginIn.asStateFlow()

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _isLoginIn.value = firebaseAuth.currentUser != null
    }


    init {
        auth.addAuthStateListener { authListener }
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener { authListener }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackBarState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()


            val authStateManager: AuthStateManager = AuthStateManager()
            val isLogIn by authStateManager.isLogIn.collectAsState()


            MyAppTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackBarState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavigation(innerPadding, isLogIn = isLogIn, onShowSnackbar = { message ->
                        scope.launch {
                            snackBarState.showSnackbar(message)
                        }
                    }
                    )
                }
            }
        }
    }
}