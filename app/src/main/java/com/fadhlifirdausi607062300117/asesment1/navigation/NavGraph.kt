package com.fadhlifirdausi607062300117.asesment1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fadhlifirdausi607062300117.asesment1.ui.screen.MainScreen

@Composable
fun SetUpNavGraph(navController: NavHostController=rememberNavController()){
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(route = Screen.Home.route){
            MainScreen()
        }
    }
}