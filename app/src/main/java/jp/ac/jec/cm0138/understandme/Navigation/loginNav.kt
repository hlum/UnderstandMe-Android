package jp.ac.jec.cm0138.understandme.Navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jp.ac.jec.cm0138.understandme.Screens.LoginScreen

fun NavGraphBuilder.loginNav(navController: NavController, modifier: Modifier = Modifier) {
    composable<LOGIN_ROUTE> {
        LoginScreen(modifier = modifier)
    }
}
