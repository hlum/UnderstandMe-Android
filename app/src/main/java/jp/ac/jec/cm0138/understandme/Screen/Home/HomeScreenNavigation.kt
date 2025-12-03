package jp.ac.jec.cm0138.understandme.Screen.Home

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jp.ac.jec.cm0138.understandme.Nav.Route.HomeRoute

fun NavGraphBuilder.homeScreen(
    modifier: Modifier = Modifier
) {
    composable<HomeRoute> { 
        HomeScreen(
            modifier = modifier
        )
    }
}