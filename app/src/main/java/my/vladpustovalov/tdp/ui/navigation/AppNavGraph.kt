package my.vladpustovalov.tdp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import my.vladpustovalov.tdp.presentation.viewmodel.AuthViewModel
import my.vladpustovalov.tdp.ui.screens.ChangePasswordScreen
import my.vladpustovalov.tdp.ui.screens.EditUserScreen
import my.vladpustovalov.tdp.ui.screens.LoginScreen
import my.vladpustovalov.tdp.ui.screens.MainScreen

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
        composable(Screen.Login.route) { LoginScreen(navController, authViewModel) }
        composable(Screen.Main.route) { MainScreen(navController, authViewModel) }
        composable(Screen.EditUser.route) { EditUserScreen(navController) }
        composable(Screen.ChangePassword.route) { ChangePasswordScreen(navController, authViewModel) }
    }
}
