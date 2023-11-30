package hu.ait.crave.ui.navigation


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Feed : Screen("feed")
    object WritePost : Screen("writepost")
}