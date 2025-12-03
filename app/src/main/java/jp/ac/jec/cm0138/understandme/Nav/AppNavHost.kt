package jp.ac.jec.cm0138.understandme.Nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import jp.ac.jec.cm0138.understandme.Screen.Home.homeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.HomeRoute
    ) {
        homeScreen()
    }
}