package com.kareemdev.themoviedb.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kareemdev.themoviedb.presentation.home.HomePage
import com.kareemdev.themoviedb.presentation.search.SearchPage
import com.kareemdev.themoviedb.presentation.splash.SplashPage

@Composable
fun SetupUpNavGraph(navHostController: NavHostController, modifier: Modifier) {
    NavHost(
        navHostController,
        startDestination = Screen.SplashScreen.route,
    ) {
        composable(route = Screen.SplashScreen.route) {
            SplashPage(navHostController)
        }
        composable(route = Screen.Home.route) {
            HomePage()
        }
        composable(route = Screen.Search.route) {
            SearchPage(
                onBack = {navHostController.popBackStack()}
            )
        }
    }
}