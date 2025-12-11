package jp.ac.jec.cm0138.understandme.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    paddingValues: PaddingValues,
    isLogIn: Boolean,
    onShowSnackbar: (message: String) -> Unit
) {
    val navController = rememberNavController()


    LaunchedEffect(isLogIn) {
        if(isLogIn) {
            navController.navigate(HOME_ROUTE) {
                popUpTo(LOGIN_ROUTE) { inclusive = true }
            }
        } else {
            navController.navigate(LOGIN_ROUTE) {
                popUpTo(HOME_ROUTE) { inclusive = true }
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = if(isLogIn) HOME_ROUTE else LOGIN_ROUTE,
    ) {
        loginNav(
            onShowSnackbar = onShowSnackbar,
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
        homeNav(navController = navController, modifier = Modifier.padding(paddingValues))
    }
}