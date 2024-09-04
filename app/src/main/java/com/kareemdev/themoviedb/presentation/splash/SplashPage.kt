package com.kareemdev.themoviedb.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.kareemdev.themoviedb.R
import com.kareemdev.themoviedb.presentation.navigation.Screen

@Composable
fun SplashPage(navController: NavHostController, ) {
    val degrees = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        degrees.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 200
            )
        )
        navController.popBackStack()
        navController.navigate(Screen.Home.route)
    }
    Splash(degrees = degrees.value)
}


@Composable
fun Splash(degrees: Float) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.rotate(degrees = degrees),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.app_name),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}