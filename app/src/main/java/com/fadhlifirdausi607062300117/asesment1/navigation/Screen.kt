package com.fadhlifirdausi607062300117.asesment1.navigation

sealed class Screen (val route : String){
    data object Home : Screen("mainScreen")
}