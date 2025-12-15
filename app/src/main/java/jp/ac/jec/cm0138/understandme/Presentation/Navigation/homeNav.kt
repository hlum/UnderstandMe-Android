package jp.ac.jec.cm0138.understandme.Presentation.Navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.homeNav(navController: NavController, modifier: Modifier = Modifier) {
    composable<HOME_ROUTE> { entry ->
        Column(
            modifier = modifier
        ) {
            Text("Home Screen")
            Button(
                onClick = {
                    navController.navigate(LOGIN_ROUTE)
                }
            ) {
                Text("Go to Login")
            }
        }

    }
}