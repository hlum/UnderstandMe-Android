package jp.ac.jec.cm0138.understandme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import jp.ac.jec.cm0138.understandme.Navigation.AppNavigation
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var snackBarState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            MyAppTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackBarState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavigation(innerPadding, onShowSnackbar = { message ->
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