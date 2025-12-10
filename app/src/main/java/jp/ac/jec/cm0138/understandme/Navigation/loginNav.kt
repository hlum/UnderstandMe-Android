package jp.ac.jec.cm0138.understandme.Navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.loginNav(navController: NavController, modifier: Modifier = Modifier) {
    composable<LOGIN_ROUTE> {
        Column(
            modifier = modifier
        ) {

            Text("Login Screen")
            Button(
                onClick = {
                    navController.navigate(HOME_ROUTE)
                }
            ) {
                Text("Go To Home")
            }
        }
    }
}
