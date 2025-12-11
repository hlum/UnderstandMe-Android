package jp.ac.jec.cm0138.understandme.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    isLogIn: Boolean,
    onShowSnackbar: (message: String) -> Unit
) {

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

        composable<CLASSES_ROUTE> { entry ->
            Text("Classes Screen")
        }

        composable<HOMEWORKS_ROUTE> { entry ->
            Text("Homeworks Screen")
        }

        composable<PROFILE_ROUTE> { entry ->
            Text("Profile Screen")
        }
    }
}