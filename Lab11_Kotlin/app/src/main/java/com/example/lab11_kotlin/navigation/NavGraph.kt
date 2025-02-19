package com.example.lab11_kotlin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lab11_kotlin.screen.EditScreen
import com.example.lab11_kotlin.screen.HomeScreen
import com.example.lab11_kotlin.screen.InsertScreen
import com.example.lab11_kotlin.screen.Screen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.Insert.route
        ) {
            InsertScreen(navController)
        }
        composable(
            route = Screen.Edit.route
        ) {
            EditScreen(navController)
        }
    }
}