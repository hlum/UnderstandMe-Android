package jp.ac.jec.cm0138.understandme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.AppNavigation
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.LOGIN_ROUTE
import jp.ac.jec.cm0138.understandme.Presentation.Navigation.bottomNavItems
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthStateManager @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _isLoginIn = MutableStateFlow((auth.currentUser != null))
    val isLogIn = _isLoginIn.asStateFlow()

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _isLoginIn.value = firebaseAuth.currentUser != null
    }


    init {
        auth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authListener)
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackBarState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()


            val authStateManager: AuthStateManager = hiltViewModel()
            val isLogIn  by authStateManager.isLogIn.collectAsStateWithLifecycle()


            MyAppTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackBarState) },
                    bottomBar = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination?.route

                        if (currentRoute != LOGIN_ROUTE::class.qualifiedName) {
                            NavigationBar {

                                bottomNavItems.forEach { item ->
                                    val selected = currentRoute == item.route

                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = {
                                            if (!selected) {
                                                navController.navigate(item.route) {
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(id = item.icon),
                                                contentDescription = item.label,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        },
                                        label = {
                                            Text(
                                                item.label,
                                                style = TextStyle(
                                                    fontSize = 10.sp,
                                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MyAppTheme.colors.accent,
                                            selectedTextColor = MyAppTheme.colors.accent,
                                            unselectedIconColor = MyAppTheme.colors.secondary,
                                            unselectedTextColor = MyAppTheme.colors.secondary,
                                        )
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        innerPadding,
                        isLogIn = isLogIn,
                        onShowSnackbar = { message ->
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
