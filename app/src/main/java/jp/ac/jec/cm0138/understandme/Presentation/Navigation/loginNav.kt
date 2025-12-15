package jp.ac.jec.cm0138.understandme.Presentation.Navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jp.ac.jec.cm0138.understandme.Presentation.Screens.LoginScreen

fun NavGraphBuilder.loginNav(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
    modifier: Modifier = Modifier
) {
    composable<LOGIN_ROUTE> {
        LoginScreen(
            modifier = modifier,
            navController = navController,
            onShowSnackbar = onShowSnackbar
        )
    }
}
