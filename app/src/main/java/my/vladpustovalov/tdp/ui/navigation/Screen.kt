package my.vladpustovalov.tdp.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
    object Program : Screen("program")
    object CreateProgram : Screen("create_program")
    object AddBlock : Screen("add_block")
    object AddTraining : Screen("add_training")
    object User : Screen("user")
    object EditUser : Screen("edit_user")
    object ChangePassword : Screen("change_password")
    object UsersControll : Screen("users_controll")
}
