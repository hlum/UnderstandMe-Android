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
import androidx.compose.ui.layout.BeyondBoundsLayout.LayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jp.ac.jec.cm0138.understandme.Presentation.Screens.AnswerQuestionsScreen
import jp.ac.jec.cm0138.understandme.Presentation.Screens.ClassListScreen
import jp.ac.jec.cm0138.understandme.Presentation.Screens.HomeScreen
import jp.ac.jec.cm0138.understandme.Presentation.Screens.HomeworkDetailScreen
import jp.ac.jec.cm0138.understandme.Presentation.Screens.HomeworkListScreen

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
                modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
            )
        }


        composable<CLASSES_ROUTE> { entry ->
            ClassListScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }

        composable<HOMEWORKS_ROUTE> { entry ->
            HomeworkListScreen(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
            )
        }

        composable<PROFILE_ROUTE> { entry ->
            Text("Profile Screen")
        }



        composable<HOMEWORK_DETAIL_ROUTE> { entry ->
            val homeworkId = entry.arguments?.getString("homeworkID") ?: ""
            HomeworkDetailScreen(
                homeworkID = homeworkId,
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }


        composable<ANSWER_QUESTIONS_ROUTE> { entry ->
            val homeworkId = entry.arguments?.getString("homeworkID") ?: ""
            val modeString = entry.arguments?.getString("mode") ?: "ANSWER"
            val mode = AnswerMode.valueOf(modeString)

            AnswerQuestionsScreen(
                homeworkID = homeworkId,
                mode = mode,
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}