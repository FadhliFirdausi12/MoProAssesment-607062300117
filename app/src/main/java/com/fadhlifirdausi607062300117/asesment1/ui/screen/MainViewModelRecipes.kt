package com.fadhlifirdausi607062300117.asesment1.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadhlifirdausi607062300117.asesment1.model.Recipes
import com.fadhlifirdausi607062300117.asesment1.network.RecipesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModelRecipes : ViewModel() {

    var data = mutableStateOf(emptyList<Recipes>())
        private set

    init {
        retrievedata()
    }

    private fun retrievedata() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                data.value = RecipesApi.service.getRecipes()
            }catch (e: Exception){
                Log.d("MainViewModelRecipes", "Failure: ${e.message}")
            }
        }
    }
}