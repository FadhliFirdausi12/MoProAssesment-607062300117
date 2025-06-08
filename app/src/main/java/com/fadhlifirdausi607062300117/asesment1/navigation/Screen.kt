package com.fadhlifirdausi607062300117.asesment1.navigation

import com.fadhlifirdausi607062300117.asesment1.ui.screen.KEY_NOTES_ID

sealed class Screen(val route: String) {
    data object Home : Screen("mainScreen")
    data object Profile : Screen("profileScreen")
    data object Setting : Screen("settingScreen")
    data object Help : Screen("helpScreen")
    data object Hydration : Screen("hydrationScreen")
    data object Notes : Screen("notesScreen")
    data object Recipes : Screen("recipesScreen")

    // Route untuk menambahkan catatan baru (tanpa parameter)
    data object NotesBaru : Screen("notesDetailScreen")

    // Route untuk mengedit/melihat catatan berdasarkan ID
    data object NotesDetail : Screen("notesDetailScreen/{$KEY_NOTES_ID}"){
        fun withId(id:Long) = "notesDetailScreen/$id"



    }
}
