package my.vladpustovalov.tdp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import my.vladpustovalov.tdp.ui.screens.*

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Main.route) { MainScreen(navController) }
        composable(Screen.Program.route) { ProgramScreen(navController) }
        composable(Screen.CreateProgram.route) { CreateProgramScreen(navController) }
        composable(Screen.AddBlock.route) { AddBlockScreen(navController) }
        composable(Screen.AddTraining.route) { AddTrainingScreen(navController) }
        composable(Screen.User.route) { UserScreen(navController) }
        composable(Screen.EditUser.route) { EditUserScreen(navController) }
        composable(Screen.ChangePassword.route) { ChangePasswordScreen(navController) }
        composable(Screen.UsersControll.route) { UsersControllScreen(navController) }
    }
}
