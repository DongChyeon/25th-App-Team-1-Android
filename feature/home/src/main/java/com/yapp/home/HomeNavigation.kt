package com.yapp.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(HomeRoute.HOME, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    composable(route = HomeRoute.HOME) {
        HomeRoute(
            padding = padding,
            modifier = modifier,
        )
    }
}

object HomeRoute {
    const val HOME = "home"
}
