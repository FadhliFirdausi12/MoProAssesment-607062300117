package com.fadhlifirdausi607062300117.asesment1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.fadhlifirdausi607062300117.asesment1.ui.screen.HelpScreen
import com.fadhlifirdausi607062300117.asesment1.ui.screen.HydrationScreen
import com.fadhlifirdausi607062300117.asesment1.ui.screen.NotesScreen
import com.fadhlifirdausi607062300117.asesment1.ui.screen.MainScreen
import com.fadhlifirdausi607062300117.asesment1.ui.screen.NotesDetailScreen
import com.fadhlifirdausi607062300117.asesment1.ui.screen.SettingScreen
import com.fadhlifirdausi607062300117.asesment1.ui.screen.KEY_NOTES_ID
import com.fadhlifirdausi607062300117.asesment1.ui.screen.RecipesScreen

@Composable
fun SetUpNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }

        composable(route = Screen.Help.route) {
            HelpScreen(navController)
        }

        composable(route = Screen.Setting.route) {
            SettingScreen(navController)
        }

        composable(route = Screen.Hydration.route) {
            HydrationScreen(navController)
        }

        composable(route = Screen.Notes.route) {
            NotesScreen(navController)
        }

        // Untuk menambahkan catatan baru
        composable(route = Screen.NotesBaru.route) {
            NotesDetailScreen(navController)
        }

        // Untuk melihat/mengedit catatan berdasarkan ID
        composable(
            route = Screen.NotesDetail.route,
            arguments = listOf(navArgument(KEY_NOTES_ID) { type = NavType.LongType })
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_NOTES_ID)
            NotesDetailScreen(navController, id)
        }

        composable(route = Screen.Recipes.route) {
            RecipesScreen(navController)
        }

    }
}
