package jp.ac.jec.cm0138.understandme.Presentation.Navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jp.ac.jec.cm0138.understandme.Presentation.Screens.HomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    isLogIn: Boolean,
    onShowSnackbar: (message: String) -> Unit
) {

    LaunchedEffect(isLogIn) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        if(isLogIn && currentRoute == LOGIN_ROUTE::class.qualifiedName) {
            navController.navigate(HOME_ROUTE) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }

            }
        } else if (!isLogIn && currentRoute != LOGIN_ROUTE::class.qualifiedName) {
            navController.navigate(LOGIN_ROUTE) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = LOGIN_ROUTE,
    ) {

        loginNav(
            onShowSnackbar = onShowSnackbar,
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )

        composable<HOME_ROUTE>(
            enterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 100))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 100))
            }
        ) { entry ->
            HomeScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }


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