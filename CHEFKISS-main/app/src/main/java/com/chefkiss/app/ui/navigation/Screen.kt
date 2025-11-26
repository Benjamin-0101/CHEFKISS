package com.chefkiss.app.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Restaurant : Screen("restaurant")
    object Camera : Screen("camera")
    object Profile : Screen("profile")
    object Assistant : Screen("assistant")
    object EditProfile : Screen("edit_profile")
    object Settings : Screen("settings")
    object MyReviews : Screen("my_reviews")
}