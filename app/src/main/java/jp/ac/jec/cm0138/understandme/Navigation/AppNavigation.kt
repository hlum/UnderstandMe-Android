package jp.ac.jec.cm0138.understandme.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(paddingValues: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LOGIN_ROUTE,
    ) {
        loginNav(navController = navController, modifier = Modifier.padding(paddingValues))
        homeNav(navController = navController, modifier = Modifier.padding(paddingValues))
    }
}