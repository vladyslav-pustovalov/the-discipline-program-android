package my.vladpustovalov.thedisciplineprogram.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.AuthViewModel
import my.vladpustovalov.thedisciplineprogram.ui.screens.ChangePasswordRoute
import my.vladpustovalov.thedisciplineprogram.ui.screens.EditUserRoute
import my.vladpustovalov.thedisciplineprogram.ui.screens.LoginRoute
import my.vladpustovalov.thedisciplineprogram.ui.screens.MainRoute

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val startDestination = if (isLoggedIn) Screen.Main.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) { LoginRoute(navController, authViewModel) }
        composable(Screen.Main.route) { MainRoute(navController) }
        composable(Screen.EditUser.route) { EditUserRoute(navController) }
        composable(Screen.ChangePassword.route) { ChangePasswordRoute(navController, authViewModel) }
    }
}
