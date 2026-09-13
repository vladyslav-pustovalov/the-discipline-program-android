package my.vladpustovalov.thedisciplineprogram.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
    object EditUser : Screen("edit_user")
    object ChangePassword : Screen("change_password")
}
