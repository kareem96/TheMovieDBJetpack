package com.kareemdev.themoviedb.presentation.navigation

sealed class Screen(val route:String) {
    object SplashScreen:Screen("splash_screen")
    object Home:Screen("home_screen")
    object Search:Screen("search_screen")
}